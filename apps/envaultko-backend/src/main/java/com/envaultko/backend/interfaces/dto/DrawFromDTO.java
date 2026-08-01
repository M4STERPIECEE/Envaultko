package com.envaultko.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record DrawFromDTO(@NotBlank String potId, @Positive long amount) {}
