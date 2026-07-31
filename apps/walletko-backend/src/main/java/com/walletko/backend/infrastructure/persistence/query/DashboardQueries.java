package com.walletko.backend.infrastructure.persistence.query;

import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.infrastructure.persistence.repository.*;
import com.walletko.backend.interfaces.dto.*;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.util.*;

@Component
public class DashboardQueries {
    private final TransactionJpaRepository txJpa;
    private final PotAllocationJpaRepository potAllocJpa;
    private final ExpenseAllocationJpaRepository expenseAllocJpa;
    private final PotJpaRepository potJpa;

    public DashboardQueries(TransactionJpaRepository txJpa,
                             PotAllocationJpaRepository potAllocJpa,
                             ExpenseAllocationJpaRepository expenseAllocJpa,
                             PotJpaRepository potJpa) {
        this.txJpa = txJpa;
        this.potAllocJpa = potAllocJpa;
        this.expenseAllocJpa = expenseAllocJpa;
        this.potJpa = potJpa;
    }

    public OverviewStatsDTO getOverviewStats(String userId) {
        var now = OffsetDateTime.now();
        var startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        long monthlyIncome = 0;
        long monthlyExpense = 0;
        long allTimeIncome = 0;
        long allTimeExpense = 0;
        long totalBalance = computeTotalBalance(userId);
        long totalAllTimeIncome = 0;
        long totalAllTimeExpense = 0;

        var txs = txJpa.findByUserId(userId);
        for (var tx : txs) {
            long amt = tx.getAmount();
            boolean isIncome = tx.getType().equals("income");
            boolean isExpense = tx.getType().equals("expense");
            if (isIncome || isExpense) {
                if (tx.getCreatedAt() != null && tx.getCreatedAt().isAfter(startOfMonth)) {
                    if (isIncome) monthlyIncome += amt;
                    else monthlyExpense += amt;
                }
                if (isIncome) totalAllTimeIncome += amt;
                else totalAllTimeExpense += amt;
            }
        }

        return new OverviewStatsDTO(totalBalance, monthlyIncome, monthlyExpense,
                                    totalAllTimeIncome, totalAllTimeExpense);
    }

    public List<TopPotDTO> listTopPots(String userId, int limit) {
        var pots = potJpa.findByUserIdAndArchivedAtIsNull(userId);
        return pots.stream()
            .map(p -> {
                long bal = potAllocJpa.sumAmountByPotId(p.getId())
                           - expenseAllocJpa.sumAmountByPotId(p.getId());
                return new TopPotDTO(p.getId(), p.getName(), p.getPercentage(),
                                     p.getColor(), p.isDefault(), bal, p.getCreatedAt());
            })
            .sorted((a, b) -> Long.compare(b.balance(), a.balance()))
            .limit(limit)
            .toList();
    }

    public List<MonthStatDTO> getYearStats(String userId, int year) {
        var start = OffsetDateTime.now().withYear(year).withMonth(1).withDayOfMonth(1)
            .withHour(0).withMinute(0).withSecond(0).withNano(0);
        var end = start.plusYears(1);

        var txs = txJpa.findByUserId(userId).stream()
            .filter(tx -> tx.getCreatedAt() != null
                   && !tx.getCreatedAt().isBefore(start)
                   && tx.getCreatedAt().isBefore(end))
            .toList();

        Map<Integer, MonthStatDTO> monthly = new TreeMap<>();
        for (int m = 1; m <= 12; m++) {
            monthly.put(m, new MonthStatDTO(m, 0, 0, 0));
        }

        for (var tx : txs) {
            int month = tx.getCreatedAt().getMonthValue();
            var existing = monthly.get(month);
            long amt = tx.getAmount();
            if ("income".equals(tx.getType())) {
                monthly.put(month, new MonthStatDTO(month,
                    existing.income() + amt, existing.expense(), existing.cumulativeNet()));
            } else if ("expense".equals(tx.getType())) {
                monthly.put(month, new MonthStatDTO(month,
                    existing.income(), existing.expense() + amt, existing.cumulativeNet()));
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

    public long computeTotalBalance(String userId) {
        var pots = potJpa.findByUserId(userId);
        return pots.stream()
            .mapToLong(p -> potAllocJpa.sumAmountByPotId(p.getId())
                           - expenseAllocJpa.sumAmountByPotId(p.getId()))
            .sum();
    }
}
