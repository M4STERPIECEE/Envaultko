package com.envaultko.backend.domain.auth;

import com.envaultko.backend.domain.shared.vo.Id;
import java.util.Optional;

public interface UserQueryPort {
    Optional<AuthenticatedUser> findById(Id id);
}
