package com.walletko.backend.domain.income;

import com.walletko.backend.domain.shared.vo.*;

public record CancellationLine(Id potId, Money amount) {}
