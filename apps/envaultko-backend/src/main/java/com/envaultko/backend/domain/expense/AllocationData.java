package com.envaultko.backend.domain.expense;

import com.envaultko.backend.domain.shared.vo.*;

public record AllocationData(Id id, Id potId, Id expenseId, Money amount,
                              Datetime createdAt, Datetime updatedAt) {}
