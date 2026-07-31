package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.domain.savedview.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.infrastructure.persistence.mapper.DomainMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class DrizzleSavedViewRepository implements SavedViewRepository {
    private final SavedViewJpaRepository jpa;

    public DrizzleSavedViewRepository(SavedViewJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(SavedView view) {
        jpa.save(DomainMapper.toJpa(view));
    }

    @Override
    public Optional<SavedView> findById(Id id, Id userId) {
        return jpa.findByIdAndUserId(id.value(), userId.value())
            .map(DomainMapper::toDomain);
    }

    @Override
    public boolean existsByName(Name name, Id userId) {
        return jpa.existsByNameAndUserId(name.value(), userId.value());
    }

    @Override
    public void remove(Id id, Id userId) {
        jpa.deleteByIdAndUserId(id.value(), userId.value());
    }

    @Override
    public List<SavedView> findAll(Id userId) {
        return jpa.findByUserId(userId.value()).stream()
            .map(DomainMapper::toDomain).toList();
    }
}
