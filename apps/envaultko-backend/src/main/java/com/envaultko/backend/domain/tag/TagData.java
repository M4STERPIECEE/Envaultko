package com.envaultko.backend.domain.tag;

import com.envaultko.backend.domain.shared.vo.*;

public record TagData(Id id, Name name, Id userId,
                      Datetime createdAt, Datetime updatedAt) {}
