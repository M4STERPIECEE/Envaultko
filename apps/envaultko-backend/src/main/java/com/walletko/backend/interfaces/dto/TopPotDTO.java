package com.walletko.backend.interfaces.dto;

import java.time.OffsetDateTime;

public record TopPotDTO(
    String id, String name, int percentage, String color,
    boolean isDefault, long balance, OffsetDateTime createdAt
) {}
