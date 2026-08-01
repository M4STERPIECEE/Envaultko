package com.envaultko.backend.application.tag;

import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.tag.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditTagService {
    private final TagRepository tagRepo;

    public EditTagService(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Transactional
    public void execute(Id tagId, Name name, Id userId) {
        var tag = tagRepo.findById(tagId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
        if (tag.name().equals(name)) return;
        if (tagRepo.existsByName(name, userId)) {
            throw new TagNameConflictError(name.value());
        }
        tag.rename(name);
        tagRepo.save(tag);
    }
}
