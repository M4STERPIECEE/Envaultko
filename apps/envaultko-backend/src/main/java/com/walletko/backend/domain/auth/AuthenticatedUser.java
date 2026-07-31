package com.walletko.backend.domain.auth;

import com.walletko.backend.domain.shared.vo.Id;

public record AuthenticatedUser(
    Id id,
    String name,
    String email,
    boolean emailVerified
) {}
