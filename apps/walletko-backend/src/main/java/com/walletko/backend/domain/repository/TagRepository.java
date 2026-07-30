package com.walletko.backend.domain.repository;

import com.walletko.backend.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    List<Tag> findByUserId(String userId);
    Optional<Tag> findByNameAndUserId(String name, String userId);
}
