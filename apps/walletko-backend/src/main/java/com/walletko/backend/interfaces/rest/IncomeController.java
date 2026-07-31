package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.income.*;
import com.walletko.backend.application.tag.ResolveOwnedTags;
import com.walletko.backend.domain.income.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.interfaces.dto.BlockingPotDTO;
import com.walletko.backend.interfaces.dto.CancelIncomeResponse;
import com.walletko.backend.interfaces.dto.CancelPreviewDTO;
import com.walletko.backend.interfaces.dto.IncomeDTO;
import com.walletko.backend.interfaces.dto.response.IdResponse;
import com.walletko.backend.interfaces.dto.request.ReceiveIncomeRequest;
import com.walletko.backend.interfaces.dto.request.UpdateIncomeRequest;
import com.walletko.backend.interfaces.mapper.IncomeViewMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/income")
public class IncomeController {
    private final IncomeRepository incomeRepo;
    private final ReceiveIncomeService receiveIncomeService;
    private final UpdateIncomeService updateIncomeService;
    private final CancelIncomeService cancelIncomeService;
    private final ResolveOwnedTags resolveOwnedTags;
    private final IncomeViewMapper incomeViewMapper;

    public IncomeController(IncomeRepository incomeRepo,
                             ReceiveIncomeService receiveIncomeService,
                             UpdateIncomeService updateIncomeService,
                             CancelIncomeService cancelIncomeService,
                             ResolveOwnedTags resolveOwnedTags,
                             IncomeViewMapper incomeViewMapper) {
        this.incomeRepo = incomeRepo;
        this.receiveIncomeService = receiveIncomeService;
        this.updateIncomeService = updateIncomeService;
        this.cancelIncomeService = cancelIncomeService;
        this.resolveOwnedTags = resolveOwnedTags;
        this.incomeViewMapper = incomeViewMapper;
    }

    private Id userId(Authentication auth) {
        return new Id((String) auth.getPrincipal());
    }

    @PostMapping
    public ResponseEntity<IdResponse> receiveIncome(Authentication auth,
                                                     @Valid @RequestBody ReceiveIncomeRequest req) {
        var userId = userId(auth);
        var tags = resolveOwnedTags.resolve(userId, req.tags().stream()
            .map(t -> new ResolveOwnedTags.TagInput(t.id(), t.name())).toList());
        var createdAt = req.createdAt() != null ? Datetime.of(req.createdAt()) : null;
        var id = receiveIncomeService.execute(
            new Name(req.name()), Money.fromCents(req.amount()), tags, userId, createdAt);
        return ResponseEntity.ok(new IdResponse(id.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeDTO> getIncome(Authentication auth, @PathVariable String id) {
        var userId = userId(auth);
        var income = incomeRepo.findOne(new Id(id), userId);
        return income
            .map(i -> ResponseEntity.ok(incomeViewMapper.toDto(i)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdResponse> updateIncome(Authentication auth,
                                                    @PathVariable String id,
                                                    @Valid @RequestBody UpdateIncomeRequest req) {
        var userId = userId(auth);
        var tags = resolveOwnedTags.resolve(userId, req.tags().stream()
            .map(t -> new ResolveOwnedTags.TagInput(t.id(), t.name())).toList());
        updateIncomeService.execute(new Id(id), new Name(req.name()),
            Datetime.of(req.date()), tags, userId);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<CancelIncomeResponse> cancelIncome(Authentication auth, @PathVariable String id) {
        try {
            cancelIncomeService.execute(new Id(id), userId(auth));
            return ResponseEntity.ok(new CancelIncomeResponse(false, null, null));
        } catch (CancelIncomeBlockedError e) {
            return ResponseEntity.status(409).body(new CancelIncomeResponse(
                true, "WOULD_CAUSE_NEGATIVE_BALANCE",
                e.pots().stream()
                    .map(p -> new BlockingPotDTO(p.name(), p.shortfall()))
                    .toList()
            ));
        }
    }

    @GetMapping("/{id}/cancel-preview")
    public ResponseEntity<CancelPreviewDTO> getCancelPreview(Authentication auth, @PathVariable String id) {
        var userId = userId(auth);
        var income = incomeRepo.findOne(new Id(id), userId);
        return income
            .map(i -> ResponseEntity.ok(incomeViewMapper.toCancelPreview(i.allocations())))
            .orElse(ResponseEntity.notFound().build());
    }
}
