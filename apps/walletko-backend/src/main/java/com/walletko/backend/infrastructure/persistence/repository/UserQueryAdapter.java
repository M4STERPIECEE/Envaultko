package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.domain.auth.AuthenticatedUser;
import com.walletko.backend.domain.auth.UserQueryPort;
import com.walletko.backend.domain.shared.vo.Id;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserQueryAdapter implements UserQueryPort {

    private final UserJpaRepository jpa;

    public UserQueryAdapter(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<AuthenticatedUser> findById(Id id) {
        return jpa.findById(id.value()).map(e -> new AuthenticatedUser(
            new Id(e.getId()),
            e.getName(),
            e.getEmail(),
            e.isEmailVerified()
        ));
    }
}
