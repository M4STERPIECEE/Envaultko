package com.walletko.backend.domain.auth;

import com.walletko.backend.domain.shared.vo.Id;
import java.util.Optional;

public interface UserQueryPort {
    Optional<AuthenticatedUser> findById(Id id);
}
