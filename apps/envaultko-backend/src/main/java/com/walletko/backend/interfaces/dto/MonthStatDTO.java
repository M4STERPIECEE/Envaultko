package com.walletko.backend.interfaces.dto;

public record MonthStatDTO(
    int month, long income, long expense, long cumulativeNet
) {}
