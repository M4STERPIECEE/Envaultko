package com.envaultko.backend.interfaces.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OtherPotDTO(@NotBlank String id, @Min(1) int percentage) {}
