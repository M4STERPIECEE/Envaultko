package com.walletko.backend.domain.pot;

import com.walletko.backend.domain.shared.vo.*;

public record TransferData(Id id, Name name, Money amount, Id userId,
                            Id fromPotId, Id toPotId, Datetime createdAt) {}
