package com.envaultko.backend.domain.pot;

import com.envaultko.backend.domain.shared.vo.*;

public record TransferData(Id id, Name name, Money amount, Id userId,
                            Id fromPotId, Id toPotId, Datetime createdAt) {}
