package com.walletko.backend.domain.pot;

import com.walletko.backend.domain.shared.vo.Money;

public record PotSnapshot(Pot pot, Money balance) {}
