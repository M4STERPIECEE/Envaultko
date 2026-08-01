package com.envaultko.backend.interfaces.dto.request;

import com.envaultko.backend.interfaces.dto.TagInputDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

public record UpdateExpenseRequest(
    @NotBlank String name,
    @NotNull OffsetDateTime date,
    List<TagInputDTO> tags
) {}
