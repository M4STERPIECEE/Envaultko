package com.walletko.backend.domain.pot;

import com.walletko.backend.domain.shared.vo.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PotCollection {
    private final List<Pot> pots;

    public PotCollection(List<Pot> pots) {
        if (pots == null || pots.isEmpty()) {
            throw new IllegalArgumentException("PotCollection must have at least one pot");
        }
        this.pots = pots;
    }

    public Pot addPot(Name name, Percentage percentage, Color color,
                      List<PotUpdate> otherPots, Id userId) {
        for (PotUpdate update : otherPots) {
            Pot pot = findPot(update.id());
            pot.adjustPercentage(new Percentage(update.percentage()));
        }
        validatePercentagesSumTo100(percentage, otherPots);

        Id id = Id.generate();
        Pot newPot = Pot.create(id, name, percentage, color, userId);
        pots.add(newPot);
        return newPot;
    }

    public void adjustRepartition(List<PotUpdate> allPots) {
        validatePercentagesSumTo100(allPots);
        for (PotUpdate update : allPots) {
            Pot pot = findPot(update.id());
            pot.adjustPercentage(new Percentage(update.percentage()));
        }
    }

    public Pot archivePot(Id potId, List<PotUpdate> remainingPercentages) {
        Pot pot = findPot(potId);
        if (pot.isDefault()) {
            throw new IllegalArgumentException("Cannot archive the default pot");
        }
        for (PotUpdate update : remainingPercentages) {
            Pot p = findPot(update.id());
            if (p.id().equals(potId)) continue;
            p.adjustPercentage(new Percentage(update.percentage()));
        }
        validatePercentagesSumTo100(remainingPercentages);
        pot.archive();
        return pot;
    }

    public Optional<Pot> findById(Id id) {
        return pots.stream().filter(p -> p.id().equals(id)).findFirst();
    }

    public List<Pot> all() {
        return List.copyOf(pots);
    }

    public List<Pot> active() {
        return pots.stream().filter(p -> !p.isArchived()).collect(Collectors.toList());
    }

    public Pot findDefault() {
        return pots.stream().filter(Pot::isDefault).findFirst()
                .orElseThrow(() -> new IllegalStateException("No default pot found"));
    }

    private Pot findPot(Id id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pot not found: " + id));
    }

    private void validatePercentagesSumTo100(Percentage newPotPercentage,
                                              List<PotUpdate> otherPots) {
        int sum = newPotPercentage.value();
        sum += otherPots.stream().mapToInt(PotUpdate::percentage).sum();
        if (sum != 100) {
            throw new IllegalArgumentException(
                "Percentages must sum to 100, got: " + sum);
        }
    }

    private void validatePercentagesSumTo100(List<PotUpdate> updates) {
        int sum = updates.stream().mapToInt(PotUpdate::percentage).sum();
        if (sum != 100) {
            throw new IllegalArgumentException(
                "Percentages must sum to 100, got: " + sum);
        }
    }

}
