package com.envaultko.backend.domain.expense;

import com.envaultko.backend.domain.shared.vo.*;

public record CancellationLine(Id potId, Money amount) {}
