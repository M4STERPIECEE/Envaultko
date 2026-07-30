package com.walletko.backend.controller;

import com.walletko.backend.domain.entity.SavedView;
import com.walletko.backend.domain.repository.SavedViewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/saved-views")
public class SavedViewController {

    private final SavedViewRepository savedViewRepository;

    public SavedViewController(SavedViewRepository savedViewRepository) {
        this.savedViewRepository = savedViewRepository;
    }

    @GetMapping
    public ResponseEntity<List<SavedView>> getSavedViews(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(savedViewRepository.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<SavedView> createSavedView(Authentication authentication, @RequestBody CreateSavedViewRequest request) {
        String userId = (String) authentication.getPrincipal();
        SavedView view = new SavedView();
        view.setId(UUID.randomUUID().toString().replace("-", ""));
        view.setUserId(userId);
        view.setName(request.name());
        view.setDescription(request.description());
        view.setNameFilter(request.nameFilter());
        if (request.tagIds() != null) {
            view.setTagIds(request.tagIds());
        }
        return ResponseEntity.ok(savedViewRepository.save(view));
    }

    public record CreateSavedViewRequest(String name, String description, String nameFilter, List<String> tagIds) {}
}
