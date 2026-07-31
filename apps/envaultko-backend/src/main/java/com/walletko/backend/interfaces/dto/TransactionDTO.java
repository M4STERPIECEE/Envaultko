package com.walletko.backend.interfaces.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record TransactionDTO(
    String id, String type, String name, long amount,
    OffsetDateTime createdAt, List<TagRefDTO> tags
) {}
