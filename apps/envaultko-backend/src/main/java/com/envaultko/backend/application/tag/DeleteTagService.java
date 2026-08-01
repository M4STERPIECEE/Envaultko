package com.envaultko.backend.application.tag;

import com.envaultko.backend.domain.shared.vo.Id;
import com.envaultko.backend.domain.tag.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteTagService {
    private final TagRepository tagRepo;

    public DeleteTagService(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Transactional
    public void execute(Id tagId, Id userId) {
        tagRepo.remove(tagId, userId);
    }
}
