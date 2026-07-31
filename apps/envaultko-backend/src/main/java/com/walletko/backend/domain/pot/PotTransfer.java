package com.walletko.backend.domain.pot;

import com.walletko.backend.domain.shared.vo.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public class PotTransfer {
    private final Id id;
    private final Name name;
    private final Money amount;
    private final Id userId;
    private final Id fromPotId;
    private final Id toPotId;
    private final Datetime createdAt;

    public static PotTransfer create(Id fromPotId, Id toPotId,
                                      Money amount, Id userId) {
        return new PotTransfer(Id.generate(), new Name("Transfer"),
                               amount, userId, fromPotId, toPotId, Datetime.now());
    }

    public TransferData data() {
        return new TransferData(id, name, amount, userId, fromPotId, toPotId, createdAt);
    }
}
