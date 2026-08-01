package com.envaultko.backend.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PotTransferRequest(
    @NotBlank String fromPotId,
    @NotBlank String toPotId,
    @Positive long amount
) {}
