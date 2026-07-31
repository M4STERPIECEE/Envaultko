package com.walletko.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.OffsetDateTime;
import java.util.List;

public record PayExpenseRequest(
    @NotBlank String name,
    List<TagInputDTO> tags,
    @NotEmpty List<DrawFromDTO> drawFrom,
    OffsetDateTime createdAt
) {}
