package com.envaultko.backend.interfaces.rest;

import com.envaultko.backend.application.tag.*;
import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.tag.*;
import com.envaultko.backend.interfaces.dto.PaginatedResponseDTO;
import com.envaultko.backend.interfaces.dto.TagRefDTO;
import com.envaultko.backend.interfaces.dto.request.AddTagRequest;
import com.envaultko.backend.interfaces.mapper.TagViewMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository tagRepo;
    private final AddTagService addTagService;
    private final EditTagService editTagService;
    private final DeleteTagService deleteTagService;
    private final TagViewMapper tagViewMapper;

    public TagController(TagRepository tagRepo, AddTagService addTagService,
                          EditTagService editTagService, DeleteTagService deleteTagService,
                          TagViewMapper tagViewMapper) {
        this.tagRepo = tagRepo;
        this.addTagService = addTagService;
        this.editTagService = editTagService;
        this.deleteTagService = deleteTagService;
        this.tagViewMapper = tagViewMapper;
    }

    private Id userId(Authentication auth) {
        return new Id((String) auth.getPrincipal());
    }

    @GetMapping
    public ResponseEntity<List<TagRefDTO>> listTags(Authentication auth) {
        var tags = tagRepo.findAll(userId(auth));
        return ResponseEntity.ok(tagViewMapper.toDtos(tags));
    }

    @GetMapping("/paged")
    public ResponseEntity<PaginatedResponseDTO<TagRefDTO>> listTagsPaged(Authentication auth,
                                                                          @RequestParam int page,
                                                                          @RequestParam int pageSize) {
        var all = tagRepo.findAll(userId(auth));
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, all.size());
        var items = tagViewMapper.toDtos(all.subList(start, end));
        return ResponseEntity.ok(new PaginatedResponseDTO<>(
            items, all.size(), Math.max(1, (int) Math.ceil((double) all.size() / pageSize))
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
