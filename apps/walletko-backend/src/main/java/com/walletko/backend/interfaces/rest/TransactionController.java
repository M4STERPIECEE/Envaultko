package com.walletko.backend.interfaces.rest;

import com.walletko.backend.infrastructure.persistence.query.TransactionQueries;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionQueries transactionQueries;

    public TransactionController(TransactionQueries transactionQueries) {
        this.transactionQueries = transactionQueries;
    }

    @GetMapping
    public ResponseEntity<?> listTransactions(
            Authentication auth,
            @RequestParam(required = false) List<String> types,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> tagIds,
            @RequestParam(defaultValue = "1") int page) {
        var userId = (String) auth.getPrincipal();
        var result = transactionQueries.listTransactions(userId, types, name, tagIds, page);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<?> searchNameSuggestions(
            Authentication auth,
            @RequestParam(defaultValue = "income") String type,
            @RequestParam(defaultValue = "") String search) {
        var userId = (String) auth.getPrincipal();
        var result = transactionQueries.searchNameSuggestions(userId, type, search);
        return ResponseEntity.ok(result);
    }
}
