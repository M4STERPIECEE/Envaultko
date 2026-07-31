package com.walletko.backend.interfaces.rest;

import com.walletko.backend.infrastructure.persistence.query.DashboardQueries;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardQueries dashboardQueries;

    public DashboardController(DashboardQueries dashboardQueries) {
        this.dashboardQueries = dashboardQueries;
    }

    @GetMapping("/overview")
    public ResponseEntity<?> getOverviewStats(Authentication auth) {
        var userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(dashboardQueries.getOverviewStats(userId));
    }

    @GetMapping("/top-pots")
    public ResponseEntity<?> listTopPots(Authentication auth,
                                          @RequestParam(defaultValue = "4") int limit) {
        var userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(dashboardQueries.listTopPots(userId, Math.min(limit, 20)));
    }

    @GetMapping("/year-stats")
    public ResponseEntity<?> getYearStats(Authentication auth, @RequestParam int year) {
        var userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(dashboardQueries.getYearStats(userId, year));
    }
}
