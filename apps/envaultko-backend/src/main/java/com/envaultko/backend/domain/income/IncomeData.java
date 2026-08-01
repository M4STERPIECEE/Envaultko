package com.envaultko.backend.domain.income;

import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.tag.Tag;
import java.util.List;

public record IncomeData(Id id, Name name, Money amount, Id userId,
                          Datetime createdAt, Datetime updatedAt,
                          List<Tag> tags, List<PotAllocation> allocations) {}
