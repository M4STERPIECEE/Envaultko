package com.envaultko.backend.domain.tag;

import com.envaultko.backend.domain.shared.vo.*;
import java.util.List;
import java.util.Optional;

public interface TagRepository {
    void save(Tag tag);
    List<Tag> findAll(Id userId);
    Optional<Tag> findById(Id id, Id userId);
    boolean existsByName(Name name, Id userId);
    void remove(Id id, Id userId);
}
