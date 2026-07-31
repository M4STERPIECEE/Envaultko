package com.walletko.backend.domain.expense;

import com.walletko.backend.domain.shared.vo.*;

public record AllocationData(Id id, Id potId, Id expenseId, Money amount,
                              Datetime createdAt, Datetime updatedAt) {}
