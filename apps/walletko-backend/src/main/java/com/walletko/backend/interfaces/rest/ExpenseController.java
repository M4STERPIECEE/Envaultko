package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.expense.*;
import com.walletko.backend.application.tag.ResolveOwnedTags;
import com.walletko.backend.domain.expense.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.interfaces.dto.DrawFromDTO;
import com.walletko.backend.interfaces.dto.PayExpenseRequest;
import com.walletko.backend.interfaces.dto.UpdateExpenseRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseRepository expenseRepo;
    private final PayExpenseService payExpenseService;
    private final UpdateExpenseService updateExpenseService;
    private final CancelExpenseService cancelExpenseService;
    private final ResolveOwnedTags resolveOwnedTags;

    public ExpenseController(ExpenseRepository expenseRepo,
                              PayExpenseService payExpenseService,
                              UpdateExpenseService updateExpenseService,
                              CancelExpenseService cancelExpenseService,
                              ResolveOwnedTags resolveOwnedTags) {
        this.expenseRepo = expenseRepo;
        this.payExpenseService = payExpenseService;
        this.updateExpenseService = updateExpenseService;
        this.cancelExpenseService = cancelExpenseService;
        this.resolveOwnedTags = resolveOwnedTags;
    }

    private Id userId(Authentication auth) {
        return new Id((String) auth.getPrincipal());
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> payExpense(Authentication auth,
                                                           @Valid @RequestBody PayExpenseRequest req) {
        var userId = userId(auth);
        var tags = resolveOwnedTags.resolve(userId, req.tags().stream()
            .map(t -> new ResolveOwnedTags.TagInput(t.id(), t.name())).toList());
        var drawFrom = req.drawFrom().stream()
            .map(d -> new DrawFrom(new Id(d.potId()), Money.fromCents(d.amount())))
            .toList();
        var createdAt = req.createdAt() != null ? Datetime.of(req.createdAt()) : null;
        var id = payExpenseService.execute(new Name(req.name()), tags, drawFrom, userId, createdAt);
        return ResponseEntity.ok(Map.of("id", id.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExpense(Authentication auth, @PathVariable String id) {
        var expense = expenseRepo.findOne(new Id(id), userId(auth));
        if (expense.isEmpty()) return ResponseEntity.notFound().build();
        var d = expense.get().data();
        return ResponseEntity.ok(Map.of(
            "id", d.id().value(), "name", d.name().value(),
            "amount", d.amount().rawCents(), "createdAt", d.createdAt().toOffsetDateTime(),
            "tags", d.tags().stream().map(t -> Map.of("id", t.data().id().value(), "name", t.data().name().value())).toList(),
            "allocations", d.allocations().stream().map(a -> Map.of("potId", a.potId().value(), "amount", a.amount().rawCents())).toList()
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateExpense(Authentication auth,
                                                              @PathVariable String id,
                                                              @Valid @RequestBody UpdateExpenseRequest req) {
        var userId = userId(auth);
        var tags = resolveOwnedTags.resolve(userId, req.tags().stream()
            .map(t -> new ResolveOwnedTags.TagInput(t.id(), t.name())).toList());
        updateExpenseService.execute(new Id(id), new Name(req.name()),
            Datetime.of(req.date()), tags, userId);
        return ResponseEntity.ok(Map.of("id", id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Map<String, String>> cancelExpense(Authentication auth,
                                                              @PathVariable String id) {
        cancelExpenseService.execute(new Id(id), userId(auth));
        return ResponseEntity.ok(Map.of("id", id));
    }

    @GetMapping("/{id}/cancel-preview")
    public ResponseEntity<?> getCancelPreview(Authentication auth, @PathVariable String id) {
        var expense = expenseRepo.findOne(new Id(id), userId(auth));
        if (expense.isEmpty()) return ResponseEntity.notFound().build();
        var allocs = expense.get().allocations().stream()
            .map(a -> Map.of("potId", a.potId().value(), "amount", a.amount().rawCents()))
            .toList();
        return ResponseEntity.ok(Map.of("allocations", allocs));
    }
}
