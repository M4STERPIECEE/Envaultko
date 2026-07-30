package com.walletko.backend.controller;

import com.walletko.backend.domain.entity.Tag;
import com.walletko.backend.domain.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getTags(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(tagRepository.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(Authentication authentication, @RequestBody CreateTagRequest request) {
        String userId = (String) authentication.getPrincipal();
        Tag tag = new Tag();
        tag.setId(UUID.randomUUID().toString().replace("-", ""));
        tag.setName(request.name());
        tag.setUserId(userId);
        return ResponseEntity.ok(tagRepository.save(tag));
    }

    public record CreateTagRequest(String name) {}
}
