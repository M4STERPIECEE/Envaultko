package com.walletko.backend.domain.expense;

import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.domain.tag.Tag;
import java.util.List;

public record ExpenseData(Id id, Name name, Money amount, Id userId,
                           Datetime createdAt, Datetime updatedAt,
                           List<Tag> tags, List<ExpenseAllocation> allocations) {}
