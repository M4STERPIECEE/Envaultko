package com.envaultko.backend.interfaces.rest;

import com.envaultko.backend.application.dashboard.DashboardQuery;
import com.envaultko.backend.application.pot.*;
import com.envaultko.backend.domain.pot.*;
import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.interfaces.dto.PotWithBalanceDTO;
import com.envaultko.backend.interfaces.dto.request.AddPotRequest;
import com.envaultko.backend.interfaces.mapper.PotViewMapper;
import com.envaultko.backend.interfaces.dto.request.ArchivePotRequest;
import com.envaultko.backend.interfaces.dto.request.EditAllocationRequest;
import com.envaultko.backend.interfaces.dto.request.EditPotRequest;
import com.envaultko.backend.interfaces.dto.request.PotTransferRequest;
import com.envaultko.backend.interfaces.dto.response.IdResponse;
import com.envaultko.backend.interfaces.dto.response.TotalBalanceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pots")
public class PotController {
    private final PotRepository potRepo;
    private final DashboardQuery dashboardQuery;
    private final AddPotService addPotService;
    private final EditPotService editPotService;
    private final EditAllocationService editAllocationService;
    private final ArchivePotService archivePotService;
    private final CreatePotTransferService createPotTransferService;
    private final PotViewMapper potViewMapper;

    public PotController(PotRepository potRepo, DashboardQuery dashboardQuery,
                          AddPotService addPotService, EditPotService editPotService,
                          EditAllocationService editAllocationService,
                          ArchivePotService archivePotService,
                          CreatePotTransferService createPotTransferService,
                          PotViewMapper potViewMapper) {
        this.potRepo = potRepo;
        this.dashboardQuery = dashboardQuery;
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
    public ResponseEntity<TotalBalanceResponse> getTotalBalance(Authentication auth) {
        var total = dashboardQuery.computeTotalBalance(userId(auth).value());
        return ResponseEntity.ok(new TotalBalanceResponse(total));
    }

    @PostMapping
    public ResponseEntity<IdResponse> addPot(Authentication auth,
                                              @Valid @RequestBody AddPotRequest req) {
        var id = addPotService.execute(
            new Name(req.name()), new Percentage(req.percentage()),
            new Color(req.color()), potViewMapper.toPotUpdatesFromOtherPots(req.otherPots()),
            userId(auth));
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
        editAllocationService.execute(
            potViewMapper.toPotUpdatesFromAllocations(req.allPots()), userId(auth));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<Void> archivePot(Authentication auth, @PathVariable String id,
                                            @Valid @RequestBody ArchivePotRequest req) {
        archivePotService.execute(new Id(id), potViewMapper.toId(req.toPotId()),
            potViewMapper.toPotUpdatesFromAllocations(req.remainingPotsPercentages()),
            userId(auth));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(Authentication auth,
                                          @Valid @RequestBody PotTransferRequest req) {
        createPotTransferService.execute(
            new Id(req.fromPotId()), new Id(req.toPotId()),
            potViewMapper.toMoney(req.amount()), userId(auth));
        return ResponseEntity.ok().build();
    }
}
