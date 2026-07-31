package com.walletko.backend.interfaces.dto;

public record ViewStatsDTO(
    long totalIncome, long totalExpense, long balance
) {}
