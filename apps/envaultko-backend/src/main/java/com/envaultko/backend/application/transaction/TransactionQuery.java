package com.envaultko.backend.application.transaction;

import com.envaultko.backend.interfaces.dto.NameSuggestionDTO;
import com.envaultko.backend.interfaces.dto.PaginatedResponseDTO;
import com.envaultko.backend.interfaces.dto.TransactionDTO;

import java.util.List;

public interface TransactionQuery {
    PaginatedResponseDTO<TransactionDTO> listTransactions(
        String userId, List<String> types, String name, List<String> tagIds, int page);

    List<NameSuggestionDTO> searchNameSuggestions(String userId, String type, String search);
}
