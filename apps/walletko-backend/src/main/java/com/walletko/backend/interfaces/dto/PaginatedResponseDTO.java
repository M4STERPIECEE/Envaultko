package com.walletko.backend.interfaces.dto;

import java.util.List;

public record PaginatedResponseDTO<T>(
    List<T> items, long total, int totalPages
) {}
