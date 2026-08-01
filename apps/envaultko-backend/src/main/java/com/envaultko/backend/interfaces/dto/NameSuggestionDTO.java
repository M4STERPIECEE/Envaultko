package com.envaultko.backend.interfaces.dto;

import java.util.List;

public record NameSuggestionDTO(
    String name, List<TagRefDTO> tags
) {}
