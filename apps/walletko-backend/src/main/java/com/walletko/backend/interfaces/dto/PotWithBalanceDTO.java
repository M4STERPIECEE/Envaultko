package com.walletko.backend.interfaces.dto;

import java.time.OffsetDateTime;

public record PotWithBalanceDTO(
    String id, String name, int percentage, String color,
    boolean isDefault, OffsetDateTime createdAt, long balance
) {}
