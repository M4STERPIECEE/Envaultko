package com.envaultko.backend.infrastructure.persistence.repository;

import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.user.UserRepository;
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
