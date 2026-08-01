package com.envaultko.backend.interfaces.dto.request;

import com.envaultko.backend.interfaces.dto.PotAllocationDTO;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ArchivePotRequest(
    String toPotId,
    @NotEmpty List<PotAllocationDTO> remainingPotsPercentages
) {}
