package com.envaultko.backend.infrastructure.persistence.repository;

import com.envaultko.backend.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {
    List<TransactionEntity> findByUserId(String userId);
    List<TransactionEntity> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<TransactionEntity> findByIdAndUserId(String id, String userId);
}
