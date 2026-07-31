package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.infrastructure.persistence.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagJpaRepository extends JpaRepository<TagEntity, String> {
    List<TagEntity> findByUserId(String userId);
    Optional<TagEntity> findByIdAndUserId(String id, String userId);
    Optional<TagEntity> findByNameAndUserId(String name, String userId);
    boolean existsByNameAndUserId(String name, String userId);
    void deleteByIdAndUserId(String id, String userId);
}
