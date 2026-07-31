package com.walletko.backend.interfaces.dto.request;

import com.walletko.backend.interfaces.dto.PotAllocationDTO;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ArchivePotRequest(
    String toPotId,
    @NotEmpty List<PotAllocationDTO> remainingPotsPercentages
) {}
