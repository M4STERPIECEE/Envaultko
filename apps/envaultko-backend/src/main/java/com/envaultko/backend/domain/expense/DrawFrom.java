package com.envaultko.backend.domain.expense;

import com.envaultko.backend.domain.shared.vo.*;

public record DrawFrom(Id potId, Money amount) {}
