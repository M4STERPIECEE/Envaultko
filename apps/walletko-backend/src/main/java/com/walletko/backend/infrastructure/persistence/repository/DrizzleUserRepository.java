package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.domain.user.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DrizzleUserRepository implements UserRepository {
    private final UserJpaRepository jpa;

    public DrizzleUserRepository(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void updateName(Id userId, Name name) {
        jpa.findById(userId.value()).ifPresent(u -> {
            u.setName(name.value());
            jpa.save(u);
        });
    }
}
