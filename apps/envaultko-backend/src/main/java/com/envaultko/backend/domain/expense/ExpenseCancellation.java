package com.envaultko.backend.domain.expense;

import com.envaultko.backend.domain.shared.vo.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class ExpenseCancellation {
    private final Id id;
    private final Id cancelsTransactionId;
    private final Name name;
    private final Money amount;
    private final Id userId;
    private final List<CancellationLine> lines;
    private final Datetime createdAt;

    public static ExpenseCancellation fromExpense(Expense expense,
                                                   Function<Id, Id> resolvePotId) {
        Map<Id, Long> grouped = expense.allocations().stream()
            .collect(Collectors.groupingBy(
                a -> resolvePotId.apply(a.potId()),
                Collectors.summingLong(a -> a.amount().rawCents())
            ));

        List<CancellationLine> lines = grouped.entrySet().stream()
            .map(e -> new CancellationLine(e.getKey(), Money.fromCents(e.getValue())))
            .toList();

        return new ExpenseCancellation(Id.generate(), expense.id(),
               new Name("Cancellation: " + expense.name().value()),
               expense.amount(), expense.userId(), lines, Datetime.now());
    }

    public CancellationData data() {
        return new CancellationData(id, cancelsTransactionId, name, amount,
                                    userId, lines, createdAt);
    }
}
