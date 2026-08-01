package com.envaultko.backend.domain.income;

import com.envaultko.backend.domain.pot.Pot;
import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class Income {
    private final Id id;
    private Name name;
    private final Money amount;
    private final Id userId;
    private final Datetime createdAt;
    private Datetime updatedAt;
    @Getter(AccessLevel.NONE)
    private final List<Tag> tags;
    @Getter(AccessLevel.NONE)
    private final List<PotAllocation> allocations;

    public static Income create(Name name, Money amount, List<Tag> tags,
                                 List<Pot> pots, Id userId) {
        return create(name, amount, tags, pots, userId, null);
    }

    public static Income create(Name name, Money amount, List<Tag> tags,
                                 List<Pot> pots, Id userId, Datetime createdAt) {
        if (pots.isEmpty()) {
            throw new IllegalStateException("No active pots to distribute income");
        }
        List<PotAllocation> allocations = new ArrayList<>();
        Id incomeId = Id.generate();
        long remainingCents = amount.rawCents();

        for (int i = 0; i < pots.size(); i++) {
            Pot pot = pots.get(i);
            long potCents;
            if (i == pots.size() - 1) {
                potCents = remainingCents;
            } else {
                Money portion = amount.percentOf(pot.percentage());
                potCents = portion.rawCents();
                remainingCents -= potCents;
            }
            allocations.add(PotAllocation.allocate(pot.id(), incomeId, Money.fromCents(potCents)));
        }

        return new Income(incomeId, name, amount, userId,
                          createdAt != null ? createdAt : Datetime.now(),
                          Datetime.now(), List.copyOf(tags),
                          List.copyOf(allocations));
    }

    public void update(Name newName, Datetime newDate, List<Tag> newTags) {
        this.name = newName;
        this.updatedAt = Datetime.now();
        this.tags.clear();
        this.tags.addAll(newTags);
    }

    public List<Tag> tags() { return List.copyOf(tags); }
    public List<PotAllocation> allocations() { return List.copyOf(allocations); }

    public IncomeData data() {
        return new IncomeData(id, name, amount, userId, createdAt, updatedAt, tags, allocations);
    }
}
