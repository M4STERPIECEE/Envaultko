package com.walletko.backend.interfaces.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record EditAllocationRequest(@NotEmpty List<PotAllocationDTO> allPots) {}
