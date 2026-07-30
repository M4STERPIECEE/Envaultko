package com.walletko.backend.domain.service;

import com.walletko.backend.domain.entity.Transaction;
import com.walletko.backend.domain.enums.TransactionType;
import com.walletko.backend.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
public class DashboardService {

    private final PotService potService;
    private final TransactionRepository transactionRepository;

    public DashboardService(PotService potService, TransactionRepository transactionRepository) {
        this.potService = potService;
        this.transactionRepository = transactionRepository;
    }

    public DashboardStatsDto getDashboardStats(String userId) {
        var pots = potService.getPotsWithBalance(userId);
        long totalBalance = pots.stream().mapToLong(PotService.PotWithBalanceDto::balance).sum();

        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        OffsetDateTime now = OffsetDateTime.now();
        YearMonth currentMonth = YearMonth.from(now);

        long monthlyIncome = 0;
        long monthlyExpense = 0;

        Map<String, MonthlyTotal> yearlyMap = new TreeMap<>();

        for (Transaction t : transactions) {
            YearMonth ym = YearMonth.from(t.getCreatedAt());

            if (ym.equals(currentMonth)) {
                if (t.getType() == TransactionType.income) {
                    monthlyIncome += t.getAmount();
                } else if (t.getType() == TransactionType.expense) {
                    monthlyExpense += t.getAmount();
                }
            }

            if (ym.getYear() == now.getYear()) {
                String key = ym.toString(); // "YYYY-MM"
                MonthlyTotal total = yearlyMap.computeIfAbsent(key, k -> new MonthlyTotal(k, 0L, 0L));
                if (t.getType() == TransactionType.income) {
                    total.income += t.getAmount();
                } else if (t.getType() == TransactionType.expense) {
                    total.expense += t.getAmount();
                }
            }
        }

        return new DashboardStatsDto(
                totalBalance,
                monthlyIncome,
                monthlyExpense,
                pots,
                new ArrayList<>(yearlyMap.values())
        );
    }

    public static class MonthlyTotal {
        public String month;
        public long income;
        public long expense;

        public MonthlyTotal(String month, long income, long expense) {
            this.month = month;
            this.income = income;
            this.expense = expense;
        }
    }

    public record DashboardStatsDto(
            long totalBalance,
            long monthlyIncome,
            long monthlyExpense,
            List<PotService.PotWithBalanceDto> pots,
            List<MonthlyTotal> yearlyChartData
    ) {}
}
