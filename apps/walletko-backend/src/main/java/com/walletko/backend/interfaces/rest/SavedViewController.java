package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.savedview.*;
import com.walletko.backend.domain.savedview.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.interfaces.dto.MonthStatDTO;
import com.walletko.backend.interfaces.dto.SavedViewListItemDTO;
import com.walletko.backend.interfaces.dto.ViewStatsDTO;
import com.walletko.backend.interfaces.dto.request.UpsertViewRequest;
import com.walletko.backend.interfaces.mapper.SavedViewDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/views")
public class SavedViewController {
    private final SavedViewRepository viewRepo;
    private final ViewQuery viewQuery;
    private final CreateViewService createViewService;
    private final UpdateViewService updateViewService;
    private final DeleteViewService deleteViewService;
    private final SavedViewDtoMapper savedViewDtoMapper;

    public SavedViewController(SavedViewRepository viewRepo, ViewQuery viewQuery,
                                CreateViewService createViewService,
                                UpdateViewService updateViewService,
                                DeleteViewService deleteViewService,
                                SavedViewDtoMapper savedViewDtoMapper) {
        this.viewRepo = viewRepo;
        this.viewQuery = viewQuery;
        this.createViewService = createViewService;
        this.updateViewService = updateViewService;
        this.deleteViewService = deleteViewService;
        this.savedViewDtoMapper = savedViewDtoMapper;
    }

    private Id userId(Authentication auth) {
        return new Id((String) auth.getPrincipal());
    }

    @GetMapping
    public ResponseEntity<List<SavedViewListItemDTO>> listViews(Authentication auth) {
        var views = viewRepo.findAll(userId(auth));
        return ResponseEntity.ok(savedViewDtoMapper.toDtos(views));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavedViewListItemDTO> getView(Authentication auth, @PathVariable String id) {
        var view = viewRepo.findById(new Id(id), userId(auth));
        return view
            .map(v -> ResponseEntity.ok(savedViewDtoMapper.toDto(v)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ViewStatsDTO> getViewStats(Authentication auth, @PathVariable String id) {
        var userId = userId(auth);
        return viewRepo.findById(new Id(id), userId)
            .map(v -> ResponseEntity.ok(viewQuery.getViewStats(
                userId.value(), v.nameFilter(), savedViewDtoMapper.tagIdValues(v))))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/year-stats")
    public ResponseEntity<List<MonthStatDTO>> getViewYearStats(Authentication auth,
                                                               @PathVariable String id,
                                                               @RequestParam int year) {
        var userId = userId(auth);
        return viewRepo.findById(new Id(id), userId)
            .map(v -> ResponseEntity.ok(viewQuery.getViewYearStats(
                userId.value(), year, v.nameFilter(), savedViewDtoMapper.tagIdValues(v))))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createView(Authentication auth,
                                            @Valid @RequestBody UpsertViewRequest req) {
        createViewService.execute(new Name(req.name()), req.description(),
            req.nameFilter(), savedViewDtoMapper.toIds(req.tagIds()), userId(auth));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateView(Authentication auth, @PathVariable String id,
                                            @Valid @RequestBody UpsertViewRequest req) {
        updateViewService.execute(new Id(id), new Name(req.name()), req.description(),
            req.nameFilter(), savedViewDtoMapper.toIds(req.tagIds()), userId(auth));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteView(Authentication auth, @PathVariable String id) {
        deleteViewService.execute(new Id(id), userId(auth));
        return ResponseEntity.ok().build();
    }
}
