package com.walletko.backend.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateNameRequest(@NotBlank String name) {}
