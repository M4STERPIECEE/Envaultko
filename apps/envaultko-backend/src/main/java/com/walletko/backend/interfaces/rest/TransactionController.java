package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.transaction.TransactionQuery;
import com.walletko.backend.interfaces.dto.NameSuggestionDTO;
import com.walletko.backend.interfaces.dto.PaginatedResponseDTO;
import com.walletko.backend.interfaces.dto.TransactionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionQuery transactionQuery;

    public TransactionController(TransactionQuery transactionQuery) {
        this.transactionQuery = transactionQuery;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<TransactionDTO>> listTransactions(
            Authentication auth,
            @RequestParam(required = false) List<String> types,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> tagIds,
            @RequestParam(defaultValue = "1") int page) {
        var userId = (String) auth.getPrincipal();
        var result = transactionQuery.listTransactions(userId, types, name, tagIds, page);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<NameSuggestionDTO>> searchNameSuggestions(
            Authentication auth,
            @RequestParam(defaultValue = "income") String type,
            @RequestParam(defaultValue = "") String search) {
        var userId = (String) auth.getPrincipal();
        var result = transactionQuery.searchNameSuggestions(userId, type, search);
        return ResponseEntity.ok(result);
    }
}
