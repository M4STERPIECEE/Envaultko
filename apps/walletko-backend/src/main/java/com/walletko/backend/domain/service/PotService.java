package com.walletko.backend.domain.service;

import com.walletko.backend.domain.entity.Pot;
import com.walletko.backend.domain.repository.ExpenseAllocationRepository;
import com.walletko.backend.domain.repository.PotAllocationRepository;
import com.walletko.backend.domain.repository.PotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class PotService {

    private final PotRepository potRepository;
    private final PotAllocationRepository potAllocationRepository;
    private final ExpenseAllocationRepository expenseAllocationRepository;

    public PotService(PotRepository potRepository,
                      PotAllocationRepository potAllocationRepository,
                      ExpenseAllocationRepository expenseAllocationRepository) {
        this.potRepository = potRepository;
        this.potAllocationRepository = potAllocationRepository;
        this.expenseAllocationRepository = expenseAllocationRepository;
    }

    public List<PotWithBalanceDto> getPotsWithBalance(String userId) {
        List<Pot> pots = potRepository.findByUserIdAndArchivedAtIsNull(userId);
        List<PotWithBalanceDto> result = new ArrayList<>();

        for (Pot pot : pots) {
            long totalIn = potAllocationRepository.sumAmountByPotId(pot.getId());
            long totalOut = expenseAllocationRepository.sumAmountByPotId(pot.getId());
            long balance = totalIn - totalOut;

            result.add(new PotWithBalanceDto(
                    pot.getId(),
                    pot.getName(),
                    pot.getPercentage(),
                    pot.getColor(),
                    pot.isDefault(),
                    balance,
                    pot.getCreatedAt()
            ));
        }

        return result;
    }

    public long calculatePotBalance(String potId) {
        long totalIn = potAllocationRepository.sumAmountByPotId(potId);
        long totalOut = expenseAllocationRepository.sumAmountByPotId(potId);
        return totalIn - totalOut;
    }

    @Transactional
    public Pot createPot(String userId, String name, Integer percentage, String color, boolean isDefault) {
        Integer currentSum = potRepository.sumActivePercentagesByUserId(userId);
        if (currentSum + percentage > 100) {
            throw new IllegalArgumentException("La somme des pourcentages des pots actifs ne peut pas dépasser 100%. (Actuel: " + currentSum + "%)");
        }

        Pot pot = new Pot();
        pot.setId(UUID.randomUUID().toString().replace("-", ""));
        pot.setUserId(userId);
        pot.setName(name);
        pot.setPercentage(percentage);
        pot.setColor(color);
        pot.setDefault(isDefault);

        return potRepository.save(pot);
    }

    @Transactional
    public Pot archivePot(String userId, String potId) {
        Pot pot = potRepository.findById(potId)
                .orElseThrow(() -> new IllegalArgumentException("Pot introuvable"));

        if (!pot.getUserId().equals(userId)) {
            throw new SecurityException("Accès non autorisé au pot");
        }

        long balance = calculatePotBalance(potId);
        if (balance > 0) {
            throw new IllegalStateException("Impossible d'archiver un pot avec un solde positif (" + balance + " €)");
        }

        pot.setArchivedAt(OffsetDateTime.now());
        return potRepository.save(pot);
    }

    public record PotWithBalanceDto(
            String id,
            String name,
            Integer percentage,
            String color,
            boolean isDefault,
            long balance,
            OffsetDateTime createdAt
    ) {}
}
