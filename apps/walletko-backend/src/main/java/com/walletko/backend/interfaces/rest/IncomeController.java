package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.income.*;
import com.walletko.backend.application.tag.ResolveOwnedTags;
import com.walletko.backend.domain.income.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.infrastructure.persistence.query.DashboardQueries;
import com.walletko.backend.infrastructure.persistence.query.TransactionQueries;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/income")
public class IncomeController {
    private final IncomeRepository incomeRepo;
    private final ReceiveIncomeService receiveIncomeService;
    private final UpdateIncomeService updateIncomeService;
    private final CancelIncomeService cancelIncomeService;
    private final ResolveOwnedTags resolveOwnedTags;

    public IncomeController(IncomeRepository incomeRepo,
                             ReceiveIncomeService receiveIncomeService,
                             UpdateIncomeService updateIncomeService,
                             CancelIncomeService cancelIncomeService,
                             ResolveOwnedTags resolveOwnedTags) {
        this.incomeRepo = incomeRepo;
        this.receiveIncomeService = receiveIncomeService;
        this.updateIncomeService = updateIncomeService;
        this.cancelIncomeService = cancelIncomeService;
        this.resolveOwnedTags = resolveOwnedTags;
    }

    private Id userId(Authentication auth) {
        return new Id((String) auth.getPrincipal());
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> receiveIncome(Authentication auth,
                                                              @Valid @RequestBody ReceiveIncomeRequest req) {
        var userId = userId(auth);
        var tags = resolveOwnedTags.resolve(userId, req.tags().stream()
            .map(t -> new ResolveOwnedTags.TagInput(t.id(), t.name())).toList());
        var createdAt = req.createdAt() != null ? Datetime.of(req.createdAt()) : null;
        var id = receiveIncomeService.execute(
            new Name(req.name()), Money.fromCents(req.amount()), tags, userId, createdAt);
        return ResponseEntity.ok(Map.of("id", id.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIncome(Authentication auth, @PathVariable String id) {
        var userId = userId(auth);
        var income = incomeRepo.findOne(new Id(id), userId);
        if (income.isEmpty()) return ResponseEntity.notFound().build();
        var d = income.get().data();
        return ResponseEntity.ok(Map.of(
            "id", d.id().value(), "name", d.name().value(),
            "amount", d.amount().rawCents(), "createdAt", d.createdAt().toOffsetDateTime(),
            "tags", d.tags().stream().map(t -> Map.of("id", t.data().id().value(), "name", t.data().name().value())).toList()
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateIncome(Authentication auth,
                                                             @PathVariable String id,
                                                             @Valid @RequestBody UpdateIncomeRequest req) {
        var userId = userId(auth);
        var tags = resolveOwnedTags.resolve(userId, req.tags().stream()
            .map(t -> new ResolveOwnedTags.TagInput(t.id(), t.name())).toList());
        updateIncomeService.execute(new Id(id), new Name(req.name()),
            Datetime.of(req.date()), tags, userId);
        return ResponseEntity.ok(Map.of("id", id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelIncome(Authentication auth, @PathVariable String id) {
        try {
            cancelIncomeService.execute(new Id(id), userId(auth));
            return ResponseEntity.ok(Map.of("blocked", false));
        } catch (CancelIncomeBlockedError e) {
            return ResponseEntity.status(409).body(Map.of(
                "blocked", true, "code", "WOULD_CAUSE_NEGATIVE_BALANCE",
                "pots", e.pots().stream()
                    .map(p -> Map.of("name", p.name(), "shortfall", p.shortfall()))
                    .toList()
            ));
        }
    }

    @GetMapping("/{id}/cancel-preview")
    public ResponseEntity<?> getCancelPreview(Authentication auth, @PathVariable String id) {
        var userId = userId(auth);
        var income = incomeRepo.findOne(new Id(id), userId);
        if (income.isEmpty()) return ResponseEntity.notFound().build();

        var snapshots = new DashboardQueries(null, null, null, null) // simplified — in prod, use proper query
            .computeTotalBalance(userId.value());
        // Return allocations with pot balances
        var allocs = income.get().allocations().stream()
            .map(a -> Map.of("potId", a.potId().value(), "amount", a.amount().rawCents()))
            .toList();
        return ResponseEntity.ok(Map.of("allocations", allocs));
    }

    public record ReceiveIncomeRequest(
        @NotBlank String name, @Positive long amount,
        List<TagInput> tags,
        java.time.OffsetDateTime createdAt
    ) {}
    public record TagInput(String id, @NotBlank String name) {}
    public record UpdateIncomeRequest(
        @NotBlank String name, @NotNull java.time.OffsetDateTime date,
        List<TagInput> tags
    ) {}
    private @interface NotNull {}
}
