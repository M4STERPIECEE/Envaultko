package com.envaultko.backend.application.tag;

import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.tag.*;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ResolveOwnedTags {
    private final TagRepository tagRepo;

    public ResolveOwnedTags(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    public record TagInput(String id, String name) {}

    public List<Tag> resolve(Id userId, List<TagInput> inputs) {
        if (inputs == null || inputs.isEmpty()) return List.of();
        List<Tag> result = new ArrayList<>();

        for (var input : inputs) {
            Tag tag = null;
            if (input.id() != null) {
                tag = tagRepo.findById(new Id(input.id()), userId).orElse(null);
            }
            if (tag == null) {
                tag = tagRepo.findAll(userId).stream()
                    .filter(t -> t.name().value().equalsIgnoreCase(input.name().trim()))
                    .findFirst().orElse(null);
            }
            if (tag == null) {
                tag = Tag.create(new Name(input.name()), userId);
            }
            result.add(tag);
        }
        return result;
    }
}
