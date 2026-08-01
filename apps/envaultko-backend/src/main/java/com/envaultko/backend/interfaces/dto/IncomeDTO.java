package com.envaultko.backend.interfaces.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record IncomeDTO(
    String id, String name, long amount,
    OffsetDateTime createdAt, List<TagRefDTO> tags
) {}
