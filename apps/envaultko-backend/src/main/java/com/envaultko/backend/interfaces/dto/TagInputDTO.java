package com.envaultko.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record TagInputDTO(String id, @NotBlank String name) {}
