package com.walletko.backend.interfaces.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record SavedViewListItemDTO(
    String id, String name, String description,
    String nameFilter, List<String> tagIds, OffsetDateTime createdAt
) {}
