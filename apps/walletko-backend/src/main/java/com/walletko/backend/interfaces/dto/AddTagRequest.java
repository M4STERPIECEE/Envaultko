package com.walletko.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record AddTagRequest(@NotBlank String name) {}
