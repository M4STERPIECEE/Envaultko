package com.envaultko.backend.domain.income;

import com.envaultko.backend.domain.shared.vo.*;

public record AllocationData(Id id, Id potId, Id incomeId, Money amount,
                              Datetime createdAt, Datetime updatedAt) {}
