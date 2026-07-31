package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.infrastructure.persistence.entity.SavedViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavedViewJpaRepository extends JpaRepository<SavedViewEntity, String> {
    List<SavedViewEntity> findByUserId(String userId);
    Optional<SavedViewEntity> findByIdAndUserId(String id, String userId);
    boolean existsByNameAndUserId(String name, String userId);
    void deleteByIdAndUserId(String id, String userId);
}
