package com.walletko.backend.interfaces.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AddPotRequest(
    @NotBlank String name,
    @NotBlank String color,
    @Min(1) @Max(99) int percentage,
    @NotEmpty List<OtherPotDTO> otherPots
) {}
