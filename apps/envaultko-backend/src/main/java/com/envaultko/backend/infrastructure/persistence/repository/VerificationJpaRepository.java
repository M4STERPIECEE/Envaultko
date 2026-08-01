package com.envaultko.backend.infrastructure.persistence.repository;

import com.envaultko.backend.infrastructure.persistence.entity.VerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VerificationJpaRepository extends JpaRepository<VerificationEntity, String> {
    Optional<VerificationEntity> findByIdentifier(String identifier);
    void deleteByIdentifier(String identifier);
}
