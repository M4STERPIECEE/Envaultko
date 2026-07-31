package com.walletko.backend.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpsertViewRequest(
    @NotBlank String name,
    String description,
    String nameFilter,
    List<String> tagIds
) {}
