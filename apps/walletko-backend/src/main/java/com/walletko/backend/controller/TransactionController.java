package com.walletko.backend.controller;

import com.walletko.backend.domain.entity.Transaction;
import com.walletko.backend.domain.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.getUserTransactions(userId));
    }

    @PostMapping("/income")
    public ResponseEntity<Transaction> createIncome(Authentication authentication, @RequestBody IncomeRequest request) {
        String userId = (String) authentication.getPrincipal();
        Transaction transaction = transactionService.createIncome(userId, request.name(), request.amount());
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/expense")
    public ResponseEntity<Transaction> createExpense(Authentication authentication, @RequestBody ExpenseRequest request) {
        String userId = (String) authentication.getPrincipal();
        Transaction transaction = transactionService.createExpense(userId, request.name(), request.amount(), request.potDeductions());
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> createTransfer(Authentication authentication, @RequestBody TransferRequest request) {
        String userId = (String) authentication.getPrincipal();
        Transaction transaction = transactionService.createTransfer(userId, request.name(), request.amount(), request.fromPotId(), request.toPotId());
        return ResponseEntity.ok(transaction);
    }

    public record IncomeRequest(String name, long amount) {}
    public record ExpenseRequest(String name, long amount, Map<String, Long> potDeductions) {}
    public record TransferRequest(String name, long amount, String fromPotId, String toPotId) {}
}
