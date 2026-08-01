package com.envaultko.backend.domain.tag;

import com.envaultko.backend.domain.shared.vo.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class Tag {
    private final Id id;
    private Name name;
    private final Id userId;
    private final Datetime createdAt;
    private Datetime updatedAt;

    public static Tag create(Name name, Id userId) {
        return new Tag(Id.generate(), name, userId, Datetime.now(), Datetime.now());
    }

    public void rename(Name newName) {
        this.name = newName;
        this.updatedAt = Datetime.now();
    }

    public TagData data() {
        return new TagData(id, name, userId, createdAt, updatedAt);
    }
}
