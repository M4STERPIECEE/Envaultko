package com.envaultko.backend.interfaces.dto;

public record OverviewStatsDTO(
    long totalBalance,
    long monthlyIncome,
    long monthlyExpense,
    long allTimeIncome,
    long allTimeExpense
) {}
