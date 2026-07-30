package com.walletko.backend.controller;

import com.walletko.backend.domain.entity.Pot;
import com.walletko.backend.domain.service.PotService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pots")
public class PotController {

    private final PotService potService;

    public PotController(PotService potService) {
        this.potService = potService;
    }

    @GetMapping
    public ResponseEntity<List<PotService.PotWithBalanceDto>> getPots(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(potService.getPotsWithBalance(userId));
    }

    @PostMapping
    public ResponseEntity<Pot> createPot(Authentication authentication, @RequestBody CreatePotRequest request) {
        String userId = (String) authentication.getPrincipal();
        Pot pot = potService.createPot(userId, request.name(), request.percentage(), request.color(), request.isDefault());
        return ResponseEntity.ok(pot);
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<Pot> archivePot(Authentication authentication, @PathVariable("id") String potId) {
        String userId = (String) authentication.getPrincipal();
        Pot archived = potService.archivePot(userId, potId);
        return ResponseEntity.ok(archived);
    }

    public record CreatePotRequest(String name, Integer percentage, String color, boolean isDefault) {}
}
