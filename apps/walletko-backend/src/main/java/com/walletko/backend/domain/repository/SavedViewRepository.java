package com.walletko.backend.domain.repository;

import com.walletko.backend.domain.entity.SavedView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedViewRepository extends JpaRepository<SavedView, String> {
    List<SavedView> findByUserId(String userId);
}
