package com.envaultko.backend.domain.expense;

import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.tag.Tag;
import java.util.List;

public record ExpenseData(Id id, Name name, Money amount, Id userId,
                           Datetime createdAt, Datetime updatedAt,
                           List<Tag> tags, List<ExpenseAllocation> allocations) {}
