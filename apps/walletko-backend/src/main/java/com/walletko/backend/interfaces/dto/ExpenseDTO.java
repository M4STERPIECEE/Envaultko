package com.walletko.backend.interfaces.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record ExpenseDTO(
    String id, String name, long amount,
    OffsetDateTime createdAt, List<TagRefDTO> tags,
    List<AllocationDTO> allocations
) {}
