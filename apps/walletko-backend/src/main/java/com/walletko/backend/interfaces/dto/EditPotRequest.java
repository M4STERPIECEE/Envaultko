package com.walletko.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record EditPotRequest(@NotBlank String name, @NotBlank String color) {}
