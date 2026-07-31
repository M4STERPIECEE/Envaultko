package com.walletko.backend.interfaces.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ArchivePotRequest(
    String toPotId,
    @NotEmpty List<PotAllocationDTO> remainingPotsPercentages
) {}
