package com.envaultko.backend.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CancelIncomeResponse(boolean blocked, String code, List<BlockingPotDTO> pots) {}
