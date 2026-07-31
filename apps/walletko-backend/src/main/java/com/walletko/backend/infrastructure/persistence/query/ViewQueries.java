package com.walletko.backend.infrastructure.persistence.query;

import com.walletko.backend.infrastructure.persistence.repository.*;
import com.walletko.backend.interfaces.dto.*;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ViewQueries {
    private final TransactionJpaRepository txJpa;
    private final TagJpaRepository tagJpa;
    private final TransactionTagJpaRepository txTagJpa;
    private final SavedViewJpaRepository viewJpa;

    public ViewQueries(TransactionJpaRepository txJpa,
                        TagJpaRepository tagJpa,
                        TransactionTagJpaRepository txTagJpa,
                        SavedViewJpaRepository viewJpa) {
        this.txJpa = txJpa;
        this.tagJpa = tagJpa;
        this.txTagJpa = txTagJpa;
        this.viewJpa = viewJpa;
    }

    public record ViewStatsDTO(long totalIncome, long totalExpense, long balance) {}

    public ViewStatsDTO getViewStats(String userId, String nameFilter, List<String> tagIds) {
        var txs = filterTransactions(txJpa.findByUserId(userId), nameFilter, tagIds);
        long totalIncome = 0, totalExpense = 0;
        for (var tx : txs) {
            if ("income".equals(tx.getType())) totalIncome += tx.getAmount();
            else if ("expense".equals(tx.getType())) totalExpense += tx.getAmount();
        }
        return new ViewStatsDTO(totalIncome, totalExpense, totalIncome - totalExpense);
    }

    public List<MonthStatDTO> getViewYearStats(String userId, int year,
                                                 String nameFilter, List<String> tagIds) {
        var allTxs = filterTransactions(txJpa.findByUserId(userId), nameFilter, tagIds);
        var start = OffsetDateTime.now().withYear(year).withMonth(1).withDayOfMonth(1)
            .withHour(0).withMinute(0).withSecond(0).withNano(0);
        var end = start.plusYears(1);

        var txs = allTxs.stream()
            .filter(tx -> tx.getCreatedAt() != null
                   && !tx.getCreatedAt().isBefore(start) && tx.getCreatedAt().isBefore(end))
            .toList();

        Map<Integer, MonthStatDTO> monthly = new TreeMap<>();
        for (int m = 1; m <= 12; m++) monthly.put(m, new MonthStatDTO(m, 0, 0, 0));

        for (var tx : txs) {
            int m = tx.getCreatedAt().getMonthValue();
            var ex = monthly.get(m);
            if ("income".equals(tx.getType())) {
                monthly.put(m, new MonthStatDTO(m, ex.income() + tx.getAmount(), ex.expense(), ex.cumulativeNet()));
            } else if ("expense".equals(tx.getType())) {
                monthly.put(m, new MonthStatDTO(m, ex.income(), ex.expense() + tx.getAmount(), ex.cumulativeNet()));
            }
        }

        long cumulative = 0;
        List<MonthStatDTO> result = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            var ms = monthly.get(m);
            cumulative += ms.income() - ms.expense();
            result.add(new MonthStatDTO(m, ms.income(), ms.expense(), cumulative));
        }
        return result;
    }

    private List<com.walletko.backend.infrastructure.persistence.entity.TransactionEntity> filterTransactions(
            List<com.walletko.backend.infrastructure.persistence.entity.TransactionEntity> txs,
            String nameFilter, List<String> tagIds) {
        var stream = txs.stream();
        if (nameFilter != null && !nameFilter.isBlank()) {
            stream = stream.filter(tx -> tx.getName().toLowerCase().contains(nameFilter.toLowerCase()));
        }
        if (tagIds != null && !tagIds.isEmpty()) {
            stream = stream.filter(tx -> {
                var tt = txTagJpa.findByTransactionId(tx.getId());
                return tt.stream().anyMatch(t -> tagIds.contains(t.getTagId()));
            });
        }
        return stream.toList();
    }
}
