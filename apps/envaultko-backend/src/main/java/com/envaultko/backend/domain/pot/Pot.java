package com.envaultko.backend.domain.pot;

import com.envaultko.backend.domain.shared.vo.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class Pot {
    private final Id id;
    private Name name;
    private Percentage percentage;
    private Color color;
    private final boolean isDefault;
    private final Id userId;
    private final Datetime createdAt;
    private Datetime updatedAt;
    private Datetime archivedAt;

    public static Pot create(Id id, Name name, Percentage percentage,
                              Color color, Id userId) {
        return new Pot(id, name, percentage, color, false, userId,
                       Datetime.now(), null, null);
    }

    public static Pot createDefault(Id id, Id userId) {
        return new Pot(id, new Name("General"), new Percentage(100),
                       new Color("#6366f1"), true, userId,
                       Datetime.now(), null, null);
    }

    public void adjustPercentage(Percentage newPercentage) {
        this.percentage = newPercentage;
        this.updatedAt = Datetime.now();
    }

    public void changeColor(Color newColor) {
        this.color = newColor;
        this.updatedAt = Datetime.now();
    }

    public void changeName(Name newName) {
        this.name = newName;
        this.updatedAt = Datetime.now();
    }

    public void archive() {
        this.archivedAt = Datetime.now();
        this.updatedAt = Datetime.now();
    }

    public boolean isArchived() {
        return archivedAt != null;
    }

    public PotData data() {
        return new PotData(id, name, percentage, color, isDefault,
                          userId, createdAt, updatedAt, archivedAt);
    }
}
