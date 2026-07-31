package com.walletko.backend.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EditPotRequest(@NotBlank String name, @NotBlank String color) {}
