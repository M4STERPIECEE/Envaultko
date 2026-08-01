package com.envaultko.backend.application.savedview;

import com.envaultko.backend.interfaces.dto.MonthStatDTO;
import com.envaultko.backend.interfaces.dto.ViewStatsDTO;

import java.util.List;

public interface ViewQuery {
    ViewStatsDTO getViewStats(String userId, String nameFilter, List<String> tagIds);

    List<MonthStatDTO> getViewYearStats(String userId, int year,
                                         String nameFilter, List<String> tagIds);
}
