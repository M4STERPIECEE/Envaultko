package com.envaultko.backend.domain.expense;

import com.envaultko.backend.domain.shared.vo.*;
import java.util.List;

public record CancellationData(Id id, Id cancelsTransactionId, Name name,
                                Money amount, Id userId,
                                List<CancellationLine> lines, Datetime createdAt) {}
