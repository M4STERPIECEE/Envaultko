package com.walletko.backend.interfaces.rest;

import com.walletko.backend.application.tag.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.domain.tag.*;
import com.walletko.backend.interfaces.dto.AddTagRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository tagRepo;
    private final AddTagService addTagService;
    private final EditTagService editTagService;
    private final DeleteTagService deleteTagService;

    public TagController(TagRepository tagRepo, AddTagService addTagService,
                          EditTagService editTagService, DeleteTagService deleteTagService) {
        this.tagRepo = tagRepo;
        this.addTagService = addTagService;
        this.editTagService = editTagService;
        this.deleteTagService = deleteTagService;
    }

    private Id userId(Authentication auth) {
        return new Id((String) auth.getPrincipal());
    }

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> listTags(Authentication auth) {
        var tags = tagRepo.findAll(userId(auth));
        return ResponseEntity.ok(tags.stream()
            .map(t -> Map.of("id", t.data().id().value(), "name", t.data().name().value()))
            .toList());
    }

    @GetMapping("/paged")
    public ResponseEntity<?> listTagsPaged(Authentication auth,
                                            @RequestParam int page,
                                            @RequestParam int pageSize) {
        var all = tagRepo.findAll(userId(auth));
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, all.size());
        var items = all.subList(start, end).stream()
            .map(t -> Map.of("id", t.data().id().value(), "name", t.data().name().value()))
            .toList();
        return ResponseEntity.ok(Map.of(
            "items", items, "total", all.size(),
            "totalPages", Math.max(1, (int) Math.ceil((double) all.size() / pageSize))
        ));
    }

    @PostMapping
    public ResponseEntity<Void> addTag(Authentication auth,
                                        @Valid @RequestBody AddTagRequest req) {
        try {
            addTagService.execute(new Name(req.name()), userId(auth));
            return ResponseEntity.ok().build();
        } catch (TagNameConflictError e) {
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editTag(Authentication auth, @PathVariable String id,
                                         @Valid @RequestBody AddTagRequest req) {
        try {
            editTagService.execute(new Id(id), new Name(req.name()), userId(auth));
            return ResponseEntity.ok().build();
        } catch (TagNameConflictError e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(Authentication auth, @PathVariable String id) {
        deleteTagService.execute(new Id(id), userId(auth));
        return ResponseEntity.ok().build();
    }
}
