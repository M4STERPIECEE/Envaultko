package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.expense.*;
import com.walletko.backend.application.tag.ResolveOwnedTags;
import com.walletko.backend.domain.expense.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.interfaces.dto.CancelPreviewDTO;
import com.walletko.backend.interfaces.dto.ExpenseDTO;
import com.walletko.backend.interfaces.dto.request.PayExpenseRequest;
import com.walletko.backend.interfaces.dto.request.UpdateExpenseRequest;
import com.walletko.backend.interfaces.dto.response.IdResponse;
import com.walletko.backend.interfaces.mapper.ExpenseViewMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseRepository expenseRepo;
    private final PayExpenseService payExpenseService;
    private final UpdateExpenseService updateExpenseService;
    private final CancelExpenseService cancelExpenseService;
    private final ResolveOwnedTags resolveOwnedTags;
    private final ExpenseViewMapper expenseViewMapper;

    public ExpenseController(ExpenseRepository expenseRepo,
                              PayExpenseService payExpenseService,
                              UpdateExpenseService updateExpenseService,
                              CancelExpenseService cancelExpenseService,
                              ResolveOwnedTags resolveOwnedTags,
                              ExpenseViewMapper expenseViewMapper) {
        this.expenseRepo = expenseRepo;
        this.payExpenseService = payExpenseService;
        this.updateExpenseService = updateExpenseService;
        this.cancelExpenseService = cancelExpenseService;
        this.resolveOwnedTags = resolveOwnedTags;
        this.expenseViewMapper = expenseViewMapper;
    }

    private Id userId(Authentication auth) {
        return new Id((String) auth.getPrincipal());
    }

    @PostMapping
    public ResponseEntity<IdResponse> payExpense(Authentication auth,
                                                  @Valid @RequestBody PayExpenseRequest req) {
        var userId = userId(auth);
        var tags = resolveOwnedTags.resolve(userId, req.tags().stream()
            .map(t -> new ResolveOwnedTags.TagInput(t.id(), t.name())).toList());
        var drawFrom = req.drawFrom().stream()
            .map(d -> new DrawFrom(new Id(d.potId()), Money.fromCents(d.amount())))
            .toList();
        var createdAt = req.createdAt() != null ? Datetime.of(req.createdAt()) : null;
        var id = payExpenseService.execute(new Name(req.name()), tags, drawFrom, userId, createdAt);
        return ResponseEntity.ok(new IdResponse(id.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpense(Authentication auth, @PathVariable String id) {
        var expense = expenseRepo.findOne(new Id(id), userId(auth));
        return expense
            .map(e -> ResponseEntity.ok(expenseViewMapper.toDto(e)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdResponse> updateExpense(Authentication auth,
                                                     @PathVariable String id,
                                                     @Valid @RequestBody UpdateExpenseRequest req) {
        var userId = userId(auth);
        var tags = resolveOwnedTags.resolve(userId, req.tags().stream()
            .map(t -> new ResolveOwnedTags.TagInput(t.id(), t.name())).toList());
        updateExpenseService.execute(new Id(id), new Name(req.name()),
            Datetime.of(req.date()), tags, userId);
        return ResponseEntity.ok(new IdResponse(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<IdResponse> cancelExpense(Authentication auth,
                                                     @PathVariable String id) {
        cancelExpenseService.execute(new Id(id), userId(auth));
        return ResponseEntity.ok(new IdResponse(id));
    }

    @GetMapping("/{id}/cancel-preview")
    public ResponseEntity<CancelPreviewDTO> getCancelPreview(Authentication auth, @PathVariable String id) {
        var expense = expenseRepo.findOne(new Id(id), userId(auth));
        return expense
            .map(e -> ResponseEntity.ok(expenseViewMapper.toCancelPreview(e.allocations())))
            .orElse(ResponseEntity.notFound().build());
    }
}
