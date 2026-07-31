package com.walletko.backend.domain.expense;

import com.walletko.backend.domain.shared.vo.*;

public record DrawFrom(Id potId, Money amount) {}
