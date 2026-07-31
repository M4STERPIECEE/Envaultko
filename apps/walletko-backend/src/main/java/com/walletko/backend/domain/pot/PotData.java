package com.walletko.backend.domain.pot;

import com.walletko.backend.domain.shared.vo.*;

public record PotData(Id id, Name name, Percentage percentage, Color color,
                      boolean isDefault, Id userId, Datetime createdAt,
                      Datetime updatedAt, Datetime archivedAt) {}
