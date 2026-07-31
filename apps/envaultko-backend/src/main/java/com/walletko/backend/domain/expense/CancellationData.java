package com.walletko.backend.domain.expense;

import com.walletko.backend.domain.shared.vo.*;
import java.util.List;

public record CancellationData(Id id, Id cancelsTransactionId, Name name,
                                Money amount, Id userId,
                                List<CancellationLine> lines, Datetime createdAt) {}
