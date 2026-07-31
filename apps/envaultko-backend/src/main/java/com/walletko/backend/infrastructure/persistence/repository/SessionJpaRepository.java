package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.infrastructure.persistence.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SessionJpaRepository extends JpaRepository<SessionEntity, String> {
    Optional<SessionEntity> findByToken(String token);
    void deleteByToken(String token);
}
