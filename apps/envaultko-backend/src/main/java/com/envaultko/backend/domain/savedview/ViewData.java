package com.envaultko.backend.domain.savedview;

import com.envaultko.backend.domain.shared.vo.*;
import java.util.List;

public record ViewData(Id id, Id userId, Name name, String description,
                        String nameFilter, List<Id> tagIds,
                        Datetime createdAt, Datetime updatedAt) {}
