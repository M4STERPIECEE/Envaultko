package com.walletko.backend.domain.income;

import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.domain.tag.Tag;
import java.util.List;

public record IncomeData(Id id, Name name, Money amount, Id userId,
                          Datetime createdAt, Datetime updatedAt,
                          List<Tag> tags, List<PotAllocation> allocations) {}
