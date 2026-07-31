package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.pot.*;
import com.walletko.backend.domain.pot.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.infrastructure.persistence.query.DashboardQueries;
import com.walletko.backend.interfaces.dto.PotWithBalanceDTO;
import com.walletko.backend.interfaces.dto.request.AddPotRequest;
import com.walletko.backend.interfaces.mapper.PotViewMapper;
import com.walletko.backend.interfaces.dto.request.ArchivePotRequest;
import com.walletko.backend.interfaces.dto.request.EditAllocationRequest;
import com.walletko.backend.interfaces.dto.request.EditPotRequest;
import com.walletko.backend.interfaces.dto.request.PotTransferRequest;
import com.walletko.backend.interfaces.dto.response.IdResponse;
import com.walletko.backend.interfaces.dto.response.TotalBalanceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pots")
public class PotController {
    private final PotRepository potRepo;
    private final DashboardQueries dashboardQueries;
    private final AddPotService addPotService;
    private final EditPotService editPotService;
    private final EditAllocationService editAllocationService;
    private final ArchivePotService archivePotService;
    private final CreatePotTransferService createPotTransferService;
    private final PotViewMapper potViewMapper;

    public PotController(PotRepository potRepo, DashboardQueries dashboardQueries,
                          AddPotService addPotService, EditPotService editPotService,
                          EditAllocationService editAllocationService,
                          ArchivePotService archivePotService,
                          CreatePotTransferService createPotTransferService,
                          PotViewMapper potViewMapper) {
        this.potRepo = potRepo;
        this.dashboardQueries = dashboardQueries;
        this.addPotService = addPotService;
        this.editPotService = editPotService;
        this.editAllocationService = editAllocationService;
        this.archivePotService = archivePotService;
        this.createPotTransferService = createPotTransferService;
        this.potViewMapper = potViewMapper;
    }

    private Id userId(Authentication auth) {
        return new Id((String) auth.getPrincipal());
    }

    @GetMapping
    public ResponseEntity<List<PotWithBalanceDTO>> listPots(Authentication auth) {
        var snapshots = potRepo.findSnapshots(userId(auth));
        return ResponseEntity.ok(potViewMapper.toDtos(snapshots));
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getTotalBalance(Authentication auth) {
        var total = dashboardQueries.computeTotalBalance(userId(auth).value());
        return ResponseEntity.ok(new TotalBalanceResponse(total));
    }

    @PostMapping
    public ResponseEntity<IdResponse> addPot(Authentication auth,
                                              @Valid @RequestBody AddPotRequest req) {
        var userId = userId(auth);
        var otherPots = req.otherPots().stream()
            .map(o -> new PotUpdate(new Id(o.id()), o.percentage()))
            .toList();
        var id = addPotService.execute(
            new Name(req.name()), new Percentage(req.percentage()),
            new Color(req.color()), otherPots, userId);
        return ResponseEntity.ok(new IdResponse(id.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editPot(Authentication auth, @PathVariable String id,
                                         @Valid @RequestBody EditPotRequest req) {
        editPotService.execute(new Id(id), new Name(req.name()),
                               new Color(req.color()), userId(auth));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/allocations")
    public ResponseEntity<Void> editAllocation(Authentication auth,
                                                @Valid @RequestBody EditAllocationRequest req) {
        var allPots = req.allPots().stream()
            .map(p -> new PotUpdate(new Id(p.id()), p.percentage()))
            .toList();
        editAllocationService.execute(allPots, userId(auth));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<Void> archivePot(Authentication auth, @PathVariable String id,
                                            @Valid @RequestBody ArchivePotRequest req) {
        var remaining = req.remainingPotsPercentages().stream()
            .map(p -> new PotUpdate(new Id(p.id()), p.percentage()))
            .toList();
        archivePotService.execute(new Id(id),
            req.toPotId() != null ? new Id(req.toPotId()) : null,
            remaining, userId(auth));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(Authentication auth,
                                          @Valid @RequestBody PotTransferRequest req) {
        createPotTransferService.execute(
            new Id(req.fromPotId()), new Id(req.toPotId()),
            Money.fromCents(req.amount()), userId(auth));
        return ResponseEntity.ok().build();
    }
}
