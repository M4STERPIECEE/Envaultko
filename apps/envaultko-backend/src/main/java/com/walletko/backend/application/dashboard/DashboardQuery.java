package com.walletko.backend.application.dashboard;

import com.walletko.backend.interfaces.dto.MonthStatDTO;
import com.walletko.backend.interfaces.dto.OverviewStatsDTO;
import com.walletko.backend.interfaces.dto.TopPotDTO;

import java.util.List;

public interface DashboardQuery {
    OverviewStatsDTO getOverviewStats(String userId);

    List<TopPotDTO> listTopPots(String userId, int limit);

    List<MonthStatDTO> getYearStats(String userId, int year);

    long computeTotalBalance(String userId);
}
