package com.envaultko.backend.interfaces.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddTagRequest(@NotBlank String name) {}
