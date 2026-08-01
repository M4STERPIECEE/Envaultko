package com.envaultko.backend.domain.income;

import com.envaultko.backend.domain.shared.vo.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class PotAllocation {
    private final Id id;
    private final Id potId;
    private final Id incomeId;
    private Money amount;
    private final Datetime createdAt;
    private Datetime updatedAt;

    public static PotAllocation allocate(Id potId, Id incomeId, Money amount) {
        return new PotAllocation(Id.generate(), potId, incomeId, amount,
                                 Datetime.now(), Datetime.now());
    }

    public AllocationData data() {
        return new AllocationData(id, potId, incomeId, amount, createdAt, updatedAt);
    }
}
