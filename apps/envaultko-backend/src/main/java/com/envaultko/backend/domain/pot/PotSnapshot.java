package com.envaultko.backend.domain.pot;

import com.envaultko.backend.domain.shared.vo.Money;

public record PotSnapshot(Pot pot, Money balance) {}
