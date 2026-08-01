package com.envaultko.backend.interfaces.rest;

import com.envaultko.backend.application.dashboard.DashboardQuery;
import com.envaultko.backend.interfaces.dto.MonthStatDTO;
import com.envaultko.backend.interfaces.dto.OverviewStatsDTO;
import com.envaultko.backend.interfaces.dto.TopPotDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardQuery dashboardQuery;

    public DashboardController(DashboardQuery dashboardQuery) {
        this.dashboardQuery = dashboardQuery;
    }

    @GetMapping("/overview")
    public ResponseEntity<OverviewStatsDTO> getOverviewStats(Authentication auth) {
        var userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(dashboardQuery.getOverviewStats(userId));
    }

    @GetMapping("/top-pots")
    public ResponseEntity<List<TopPotDTO>> listTopPots(Authentication auth,
                                                       @RequestParam(defaultValue = "4") int limit) {
        var userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(dashboardQuery.listTopPots(userId, Math.min(limit, 20)));
    }

    @GetMapping("/year-stats")
    public ResponseEntity<List<MonthStatDTO>> getYearStats(Authentication auth, @RequestParam int year) {
        var userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(dashboardQuery.getYearStats(userId, year));
    }
}
