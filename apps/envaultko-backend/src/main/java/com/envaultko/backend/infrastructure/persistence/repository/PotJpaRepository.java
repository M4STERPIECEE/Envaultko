package com.envaultko.backend.infrastructure.persistence.repository;

import com.envaultko.backend.infrastructure.persistence.entity.PotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PotJpaRepository extends JpaRepository<PotEntity, String> {
    List<PotEntity> findByUserIdAndArchivedAtIsNull(String userId);
    List<PotEntity> findByUserId(String userId);
    Optional<PotEntity> findByIdAndUserId(String id, String userId);
    Optional<PotEntity> findByUserIdAndIsDefaultTrue(String userId);
}
