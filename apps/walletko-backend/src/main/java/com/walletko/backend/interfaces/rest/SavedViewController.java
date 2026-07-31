package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.savedview.*;
import com.walletko.backend.domain.savedview.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.infrastructure.persistence.query.ViewQueries;
import com.walletko.backend.interfaces.dto.UpsertViewRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/views")
public class SavedViewController {
    private final SavedViewRepository viewRepo;
    private final ViewQueries viewQueries;
    private final CreateViewService createViewService;
    private final UpdateViewService updateViewService;
    private final DeleteViewService deleteViewService;

    public SavedViewController(SavedViewRepository viewRepo, ViewQueries viewQueries,
                                CreateViewService createViewService,
                                UpdateViewService updateViewService,
                                DeleteViewService deleteViewService) {
        this.viewRepo = viewRepo;
        this.viewQueries = viewQueries;
        this.createViewService = createViewService;
        this.updateViewService = updateViewService;
        this.deleteViewService = deleteViewService;
    }

    private Id userId(Authentication auth) {
        return new Id((String) auth.getPrincipal());
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listViews(Authentication auth) {
        var views = viewRepo.findAll(userId(auth));
        return ResponseEntity.ok(views.stream()
            .map(v -> {
                var d = v.data();
                return Map.<String, Object>of(
                    "id", d.id().value(), "name", d.name().value(),
                    "description", d.description(), "nameFilter", d.nameFilter(),
                    "tagIds", d.tagIds().stream().map(Id::value).toList(),
                    "createdAt", d.createdAt().toOffsetDateTime());
            })
            .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getView(Authentication auth, @PathVariable String id) {
        var view = viewRepo.findById(new Id(id), userId(auth));
        if (view.isEmpty()) return ResponseEntity.notFound().build();
        var d = view.get().data();
        return ResponseEntity.ok(Map.of(
            "id", d.id().value(), "name", d.name().value(),
            "description", d.description(), "nameFilter", d.nameFilter(),
            "tagIds", d.tagIds().stream().map(Id::value).toList(),
            "createdAt", d.createdAt().toOffsetDateTime()));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getViewStats(Authentication auth, @PathVariable String id) {
        var userId = userId(auth);
        var view = viewRepo.findById(new Id(id), userId);
        if (view.isEmpty()) return ResponseEntity.notFound().build();
        var d = view.get().data();
        var stats = viewQueries.getViewStats(userId.value(),
            d.nameFilter(), d.tagIds().stream().map(Id::value).toList());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}/year-stats")
    public ResponseEntity<?> getViewYearStats(Authentication auth, @PathVariable String id,
                                               @RequestParam int year) {
        var userId = userId(auth);
        var view = viewRepo.findById(new Id(id), userId);
        if (view.isEmpty()) return ResponseEntity.notFound().build();
        var d = view.get().data();
        var stats = viewQueries.getViewYearStats(userId.value(), year,
            d.nameFilter(), d.tagIds().stream().map(Id::value).toList());
        return ResponseEntity.ok(stats);
    }

    @PostMapping
    public ResponseEntity<Void> createView(Authentication auth,
                                            @Valid @RequestBody UpsertViewRequest req) {
        try {
            createViewService.execute(new Name(req.name()), req.description(),
                req.nameFilter(),
                req.tagIds() != null ? req.tagIds().stream().map(Id::new).toList() : List.of(),
                userId(auth));
            return ResponseEntity.ok().build();
        } catch (SavedViewNameConflictError e) {
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateView(Authentication auth, @PathVariable String id,
                                            @Valid @RequestBody UpsertViewRequest req) {
        try {
            updateViewService.execute(new Id(id), new Name(req.name()), req.description(),
                req.nameFilter(),
                req.tagIds() != null ? req.tagIds().stream().map(Id::new).toList() : List.of(),
                userId(auth));
            return ResponseEntity.ok().build();
        } catch (SavedViewNameConflictError e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteView(Authentication auth, @PathVariable String id) {
        deleteViewService.execute(new Id(id), userId(auth));
        return ResponseEntity.ok().build();
    }
}
