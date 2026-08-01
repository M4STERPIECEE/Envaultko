package com.envaultko.backend.domain.auth;

import com.envaultko.backend.domain.shared.vo.Id;

public record AuthenticatedUser(
    Id id,
    String name,
    String email,
    boolean emailVerified
) {}
