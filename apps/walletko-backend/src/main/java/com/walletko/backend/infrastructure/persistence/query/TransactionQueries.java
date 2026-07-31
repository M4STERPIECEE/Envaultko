package com.walletko.backend.infrastructure.persistence.query;

import com.walletko.backend.infrastructure.persistence.repository.*;
import com.walletko.backend.interfaces.dto.*;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class TransactionQueries {
    private static final int PAGE_SIZE = 20;

    private final TransactionJpaRepository txJpa;
    private final TagJpaRepository tagJpa;
    private final TransactionTagJpaRepository txTagJpa;

    public TransactionQueries(TransactionJpaRepository txJpa,
                               TagJpaRepository tagJpa,
                               TransactionTagJpaRepository txTagJpa) {
        this.txJpa = txJpa;
        this.tagJpa = tagJpa;
        this.txTagJpa = txTagJpa;
    }

    public PaginatedResponseDTO<TransactionDTO> listTransactions(
            String userId, List<String> types, String name,
            List<String> tagIds, int page) {
        var allTxs = txJpa.findByUserIdOrderByCreatedAtDesc(userId);
        var stream = allTxs.stream();

        if (types != null && !types.isEmpty()) {
            stream = stream.filter(tx -> types.contains(tx.getType()));
        }
        if (name != null && !name.isBlank()) {
            stream = stream.filter(tx -> tx.getName().toLowerCase().contains(name.toLowerCase()));
        }
        if (tagIds != null && !tagIds.isEmpty()) {
            stream = stream.filter(tx -> {
                var txTags = txTagJpa.findByTransactionId(tx.getId());
                return txTags.stream().anyMatch(tt -> tagIds.contains(tt.getTagId()));
            });
        }

        var filtered = stream.toList();
        int total = filtered.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, total);

        var items = filtered.subList(start, end).stream()
            .map(tx -> {
                var tags = txTagJpa.findByTransactionId(tx.getId()).stream()
                    .map(tt -> tagJpa.findById(tt.getTagId()))
                    .filter(Optional::isPresent)
                    .map(opt -> new TagRefDTO(opt.get().getId(), opt.get().getName()))
                    .toList();
                return new TransactionDTO(tx.getId(), tx.getType(), tx.getName(),
                                          tx.getAmount(), tx.getCreatedAt(), tags);
            })
            .toList();

        return new PaginatedResponseDTO<>(items, total, totalPages);
    }

    public List<NameSuggestionDTO> searchNameSuggestions(
            String userId, String type, String search) {
        var allTxs = txJpa.findByUserId(userId).stream()
            .filter(tx -> tx.getType().equals(type))
            .toList();

        if (search != null && !search.isBlank()) {
            allTxs = allTxs.stream()
                .filter(tx -> tx.getName().toLowerCase().contains(search.toLowerCase()))
                .toList();
        }

        Map<String, Set<TagRefDTO>> grouped = new LinkedHashMap<>();
        for (var tx : allTxs) {
            var tags = txTagJpa.findByTransactionId(tx.getId()).stream()
                .map(tt -> tagJpa.findById(tt.getTagId()))
                .filter(Optional::isPresent)
                .map(opt -> new TagRefDTO(opt.get().getId(), opt.get().getName()))
                .collect(java.util.stream.Collectors.toSet());
            grouped.merge(tx.getName(), tags, (a, b) -> { a.addAll(b); return a; });
        }

        return grouped.entrySet().stream()
            .limit(20)
            .map(e -> new NameSuggestionDTO(e.getKey(), List.copyOf(e.getValue())))
            .toList();
    }
}
