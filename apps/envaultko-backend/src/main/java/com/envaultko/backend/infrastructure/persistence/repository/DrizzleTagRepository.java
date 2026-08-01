package com.envaultko.backend.infrastructure.persistence.repository;

import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.tag.*;
import com.envaultko.backend.infrastructure.persistence.mapper.DomainMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class DrizzleTagRepository implements TagRepository {
    private final TagJpaRepository jpa;

    public DrizzleTagRepository(TagJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(Tag tag) {
        jpa.save(DomainMapper.toJpa(tag));
    }

    @Override
    public List<Tag> findAll(Id userId) {
        return jpa.findByUserId(userId.value()).stream()
            .map(DomainMapper::toDomain).toList();
    }

    @Override
    public Optional<Tag> findById(Id id, Id userId) {
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
}
