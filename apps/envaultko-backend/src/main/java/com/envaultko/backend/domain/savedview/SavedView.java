package com.envaultko.backend.domain.savedview;

import com.envaultko.backend.domain.shared.vo.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.List;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class SavedView {
    private final Id id;
    private final Id userId;
    private Name name;
    private String description;
    private String nameFilter;
    private List<Id> tagIds;
    private final Datetime createdAt;
    private Datetime updatedAt;

    public static SavedView create(Name name, Id userId) {
        return create(name, userId, null, null, List.of());
    }

    public static SavedView create(Name name, Id userId, String description,
                                    String nameFilter, List<Id> tagIds) {
        return new SavedView(Id.generate(), userId, name, description,
                             nameFilter, List.copyOf(tagIds),
                             Datetime.now(), Datetime.now());
    }

    public void update(Name newName, String newDescription,
                        String newNameFilter, List<Id> newTagIds) {
        this.name = newName;
        this.description = newDescription;
        this.nameFilter = newNameFilter;
        this.tagIds = List.copyOf(newTagIds);
        this.updatedAt = Datetime.now();
    }

    public ViewData data() {
        return new ViewData(id, userId, name, description, nameFilter, tagIds, createdAt, updatedAt);
    }
}
