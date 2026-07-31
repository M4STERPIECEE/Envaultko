package com.walletko.backend.interfaces.dto.request;

import com.walletko.backend.interfaces.dto.DrawFromDTO;
import com.walletko.backend.interfaces.dto.TagInputDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.OffsetDateTime;
import java.util.List;

public record PayExpenseRequest(
    @NotBlank String name,
    List<TagInputDTO> tags,
    @NotEmpty List<DrawFromDTO> drawFrom,
    OffsetDateTime createdAt
) {}
