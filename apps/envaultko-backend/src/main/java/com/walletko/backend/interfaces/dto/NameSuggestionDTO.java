package com.walletko.backend.interfaces.dto;

import java.util.List;

public record NameSuggestionDTO(
    String name, List<TagRefDTO> tags
) {}
