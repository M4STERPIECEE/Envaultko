package com.walletko.backend.domain.income;

import com.walletko.backend.domain.shared.vo.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.List;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class IncomeCancellation {
    private final Id id;
    private final Id cancelsTransactionId;
    private final Name name;
    private final Money amount;
    private final Id userId;
    private final List<CancellationLine> lines;
    private final Datetime createdAt;

    public static IncomeCancellation fromIncome(Income income) {
        List<CancellationLine> lines = income.allocations().stream()
            .map(a -> new CancellationLine(a.potId(), a.amount()))
            .toList();
        return new IncomeCancellation(Id.generate(), income.id(),
               new Name("Cancellation: " + income.name().value()),
               income.amount(), income.userId(), lines, Datetime.now());
    }

    public CancellationData data() {
        return new CancellationData(id, cancelsTransactionId, name, amount,
                                    userId, lines, createdAt);
    }
}
