package com.walletko.backend.interfaces.dto.request;

import com.walletko.backend.interfaces.dto.TagInputDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.OffsetDateTime;
import java.util.List;

public record ReceiveIncomeRequest(
    @NotBlank String name,
    @Positive long amount,
    List<TagInputDTO> tags,
    OffsetDateTime createdAt
) {}
