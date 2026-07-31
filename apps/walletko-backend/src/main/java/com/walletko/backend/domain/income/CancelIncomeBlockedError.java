package com.walletko.backend.domain.income;

import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CancelIncomeBlockedError extends RuntimeException {
    private final List<BlockingPot> pots;

    public CancelIncomeBlockedError(List<BlockingPot> pots) {
        super("Income cancellation would cause negative balance in some pots");
        this.pots = pots;
    }
}
