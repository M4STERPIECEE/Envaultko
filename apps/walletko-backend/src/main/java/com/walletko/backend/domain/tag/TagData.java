package com.walletko.backend.domain.tag;

import com.walletko.backend.domain.shared.vo.*;

public record TagData(Id id, Name name, Id userId,
                      Datetime createdAt, Datetime updatedAt) {}
