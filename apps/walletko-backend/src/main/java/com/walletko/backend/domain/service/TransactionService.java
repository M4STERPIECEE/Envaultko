package com.walletko.backend.domain.service;

import com.walletko.backend.domain.entity.*;
import com.walletko.backend.domain.enums.TransactionType;
import com.walletko.backend.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final PotRepository potRepository;
    private final PotAllocationRepository potAllocationRepository;
    private final ExpenseAllocationRepository expenseAllocationRepository;
    private final PotService potService;

    public TransactionService(TransactionRepository transactionRepository,
                               PotRepository potRepository,
                               PotAllocationRepository potAllocationRepository,
                               ExpenseAllocationRepository expenseAllocationRepository,
                               PotService potService) {
        this.transactionRepository = transactionRepository;
        this.potRepository = potRepository;
        this.potAllocationRepository = potAllocationRepository;
        this.expenseAllocationRepository = expenseAllocationRepository;
        this.potService = potService;
    }

    public List<Transaction> getUserTransactions(String userId) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public Transaction createIncome(String userId, String name, long amount) {
        List<Pot> activePots = potRepository.findByUserIdAndArchivedAtIsNull(userId);
        if (activePots.isEmpty()) {
            throw new IllegalStateException("Aucun pot actif trouvé pour ventiler le revenu");
        }

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString().replace("-", ""));
        transaction.setUserId(userId);
        transaction.setName(name);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.income);
        Transaction saved = transactionRepository.save(transaction);

        long remainingAmount = amount;
        for (int i = 0; i < activePots.size(); i++) {
            Pot pot = activePots.get(i);
            long potAmount;
            if (i == activePots.size() - 1) {
                potAmount = remainingAmount;
            } else {
                potAmount = Math.round(amount * (pot.getPercentage() / 100.0));
                remainingAmount -= potAmount;
            }

            PotAllocation allocation = new PotAllocation();
            allocation.setId(UUID.randomUUID().toString().replace("-", ""));
            allocation.setTransactionId(saved.getId());
            allocation.setPotId(pot.getId());
            allocation.setAmount(potAmount);
            potAllocationRepository.save(allocation);
        }

        return saved;
    }

    @Transactional
    public Transaction createExpense(String userId, String name, long amount, Map<String, Long> potDeductions) {
        long totalDeduction = potDeductions.values().stream().mapToLong(Long::longValue).sum();
        if (totalDeduction != amount) {
            throw new IllegalArgumentException("La somme des déductions par pot (" + totalDeduction + " €) ne correspond pas au montant total de la dépense (" + amount + " €)");
        }

        for (Map.Entry<String, Long> entry : potDeductions.entrySet()) {
            String potId = entry.getKey();
            Long required = entry.getValue();
            long currentBalance = potService.calculatePotBalance(potId);
            if (currentBalance < required) {
                throw new IllegalStateException("Solde insuffisant dans le pot pour couvrir la dépense (Solde disponible: " + currentBalance + " €)");
            }
        }

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString().replace("-", ""));
        transaction.setUserId(userId);
        transaction.setName(name);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.expense);
        Transaction saved = transactionRepository.save(transaction);

        for (Map.Entry<String, Long> entry : potDeductions.entrySet()) {
            ExpenseAllocation allocation = new ExpenseAllocation();
            allocation.setId(UUID.randomUUID().toString().replace("-", ""));
            allocation.setTransactionId(saved.getId());
            allocation.setPotId(entry.getKey());
            allocation.setAmount(entry.getValue());
            expenseAllocationRepository.save(allocation);
        }

        return saved;
    }

    @Transactional
    public Transaction createTransfer(String userId, String name, long amount, String fromPotId, String toPotId) {
        long fromBalance = potService.calculatePotBalance(fromPotId);
        if (fromBalance < amount) {
            throw new IllegalStateException("Solde insuffisant dans le pot source pour le transfert (" + fromBalance + " €)");
        }

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString().replace("-", ""));
        transaction.setUserId(userId);
        transaction.setName(name);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.transfer);
        Transaction saved = transactionRepository.save(transaction);

        ExpenseAllocation outAlloc = new ExpenseAllocation();
        outAlloc.setId(UUID.randomUUID().toString().replace("-", ""));
        outAlloc.setTransactionId(saved.getId());
        outAlloc.setPotId(fromPotId);
        outAlloc.setAmount(amount);
        expenseAllocationRepository.save(outAlloc);

        PotAllocation inAlloc = new PotAllocation();
        inAlloc.setId(UUID.randomUUID().toString().replace("-", ""));
        inAlloc.setTransactionId(saved.getId());
        inAlloc.setPotId(toPotId);
        inAlloc.setAmount(amount);
        potAllocationRepository.save(inAlloc);

        return saved;
    }
}
