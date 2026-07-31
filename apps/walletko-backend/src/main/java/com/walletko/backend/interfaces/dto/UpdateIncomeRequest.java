package com.walletko.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

public record UpdateIncomeRequest(
    @NotBlank String name,
    @NotNull OffsetDateTime date,
    List<TagInputDTO> tags
) {}
