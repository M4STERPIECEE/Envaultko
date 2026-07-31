package com.walletko.backend.domain.expense;

import com.walletko.backend.domain.shared.vo.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class ExpenseAllocation {
    private final Id id;
    private final Id potId;
    private final Id expenseId;
    private Money amount;
    private final Datetime createdAt;
    private Datetime updatedAt;

    public static ExpenseAllocation allocate(Id potId, Id expenseId, Money amount) {
        return new ExpenseAllocation(Id.generate(), potId, expenseId, amount,
                                     Datetime.now(), Datetime.now());
    }

    public AllocationData data() {
        return new AllocationData(id, potId, expenseId, amount, createdAt, updatedAt);
    }
}
