package com.walletko.backend.domain.expense;

import com.walletko.backend.domain.pot.PotSnapshot;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class Expense {
    private final Id id;
    private Name name;
    private final Money amount;
    private final Id userId;
    private final Datetime createdAt;
    private Datetime updatedAt;
    @Getter(AccessLevel.NONE)
    private final List<Tag> tags;
    @Getter(AccessLevel.NONE)
    private final List<ExpenseAllocation> allocations;

    public static Expense create(Name name, List<Tag> tags,
                                  List<DrawFrom> selectedPots,
                                  List<PotSnapshot> pots, Id userId) {
        return create(name, tags, selectedPots, pots, userId, null);
    }

    public static Expense create(Name name, List<Tag> tags,
                                  List<DrawFrom> selectedPots,
                                  List<PotSnapshot> pots, Id userId,
                                  Datetime createdAt) {
        if (selectedPots == null || selectedPots.isEmpty()) {
            throw new IllegalArgumentException("At least one pot must be selected");
        }

        Money totalAmount = Money.fromCents(selectedPots.stream()
            .mapToLong(df -> df.amount().rawCents()).sum());

        List<ExpenseAllocation> allocations = new ArrayList<>();
        Id expenseId = Id.generate();

        for (DrawFrom df : selectedPots) {
            PotSnapshot potSnap = pots.stream()
                .filter(p -> p.pot().id().equals(df.potId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pot not found: " + df.potId()));

            if (df.amount().isLessThan(Money.fromCents(1))) {
                throw new IllegalArgumentException("Allocation amount must be positive");
            }
            if (potSnap.balance().isLessThan(df.amount())) {
                throw new IllegalArgumentException(
                    "Insufficient balance in pot " + potSnap.pot().name().value()
                    + " (available: " + potSnap.balance().rawCents()
                    + ", required: " + df.amount().rawCents() + ")");
            }

            allocations.add(ExpenseAllocation.allocate(df.potId(), expenseId, df.amount()));
        }

        return new Expense(expenseId, name, totalAmount, userId,
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
    public List<ExpenseAllocation> allocations() { return List.copyOf(allocations); }

    public ExpenseData data() {
        return new ExpenseData(id, name, amount, userId, createdAt, updatedAt, tags, allocations);
    }
}
