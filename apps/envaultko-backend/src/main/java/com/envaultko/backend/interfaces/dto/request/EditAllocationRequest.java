package com.envaultko.backend.interfaces.dto.request;

import com.envaultko.backend.interfaces.dto.PotAllocationDTO;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record EditAllocationRequest(@NotEmpty List<PotAllocationDTO> allPots) {}
