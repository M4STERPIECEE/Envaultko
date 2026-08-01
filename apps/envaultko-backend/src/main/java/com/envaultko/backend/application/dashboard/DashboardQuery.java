package com.envaultko.backend.application.dashboard;

import com.envaultko.backend.interfaces.dto.MonthStatDTO;
import com.envaultko.backend.interfaces.dto.OverviewStatsDTO;
import com.envaultko.backend.interfaces.dto.TopPotDTO;

import java.util.List;

public interface DashboardQuery {
    OverviewStatsDTO getOverviewStats(String userId);

    List<TopPotDTO> listTopPots(String userId, int limit);

    List<MonthStatDTO> getYearStats(String userId, int year);

    long computeTotalBalance(String userId);
}
