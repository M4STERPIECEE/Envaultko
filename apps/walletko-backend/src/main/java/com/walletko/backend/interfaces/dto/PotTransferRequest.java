package com.walletko.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PotTransferRequest(
    @NotBlank String fromPotId,
    @NotBlank String toPotId,
    @Positive long amount
) {}
