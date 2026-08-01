package com.envaultko.backend.interfaces.rest;

import com.envaultko.backend.application.income.*;
import com.envaultko.backend.application.tag.ResolveOwnedTags;
import com.envaultko.backend.domain.income.*;
import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.interfaces.dto.CancelIncomeResponse;
import com.envaultko.backend.interfaces.dto.CancelPreviewDTO;
import com.envaultko.backend.interfaces.dto.IncomeDTO;
import com.envaultko.backend.interfaces.dto.response.IdResponse;
import com.envaultko.backend.interfaces.dto.request.ReceiveIncomeRequest;
import com.envaultko.backend.interfaces.dto.request.UpdateIncomeRequest;
import com.envaultko.backend.interfaces.mapper.IncomeViewMapper;
import com.envaultko.backend.interfaces.mapper.RequestMapper;
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
    private final RequestMapper requestMapper;

    public IncomeController(IncomeRepository incomeRepo,
                             ReceiveIncomeService receiveIncomeService,
                             UpdateIncomeService updateIncomeService,
                             CancelIncomeService cancelIncomeService,
                             ResolveOwnedTags resolveOwnedTags,
                             IncomeViewMapper incomeViewMapper,
                             RequestMapper requestMapper) {
        this.incomeRepo = incomeRepo;
        this.receiveIncomeService = receiveIncomeService;
        this.updateIncomeService = updateIncomeService;
        this.cancelIncomeService = cancelIncomeService;
        this.resolveOwnedTags = resolveOwnedTags;
        this.incomeViewMapper = incomeViewMapper;
        this.requestMapper = requestMapper;
    }

    private Id userId(Authentication auth) {
        return new Id((String) auth.getPrincipal());
    }

    @PostMapping
    public ResponseEntity<IdResponse> receiveIncome(Authentication auth,
                                                     @Valid @RequestBody ReceiveIncomeRequest req) {
        var userId = userId(auth);
        var tags = resolveOwnedTags.resolve(userId, requestMapper.toTagInputs(req.tags()));
        var createdAt = requestMapper.toDatetime(req.createdAt());
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
        var tags = resolveOwnedTags.resolve(userId, requestMapper.toTagInputs(req.tags()));
        updateIncomeService.execute(new Id(id), new Name(req.name()),
            requestMapper.toDatetime(req.date()), tags, userId);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<CancelIncomeResponse> cancelIncome(Authentication auth, @PathVariable String id) {
        cancelIncomeService.execute(new Id(id), userId(auth));
        return ResponseEntity.ok(new CancelIncomeResponse(false, null, null));
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
