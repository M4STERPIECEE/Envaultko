package com.walletko.backend.application.tag;

import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.domain.tag.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddTagService {
    private final TagRepository tagRepo;

    public AddTagService(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Transactional
    public void execute(Name name, Id userId) {
        if (tagRepo.existsByName(name, userId)) {
            throw new TagNameConflictError(name.value());
        }
        var tag = Tag.create(name, userId);
        tagRepo.save(tag);
    }
}
