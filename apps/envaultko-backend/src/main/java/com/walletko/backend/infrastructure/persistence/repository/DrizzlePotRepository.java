package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.domain.pot.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.infrastructure.persistence.mapper.DomainMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DrizzlePotRepository implements PotRepository {
    private final PotJpaRepository jpa;
    private final PotAllocationJpaRepository potAllocJpa;
    private final ExpenseAllocationJpaRepository expenseAllocJpa;

    public DrizzlePotRepository(PotJpaRepository jpa,
                                 PotAllocationJpaRepository potAllocJpa,
                                 ExpenseAllocationJpaRepository expenseAllocJpa) {
        this.jpa = jpa;
        this.potAllocJpa = potAllocJpa;
        this.expenseAllocJpa = expenseAllocJpa;
    }

    @Override
    public void save(Pot pot) {
        jpa.save(DomainMapper.toJpa(pot));
    }

    @Override
    public List<Pot> findAll(Id userId) {
        return jpa.findByUserIdAndArchivedAtIsNull(userId.value()).stream()
            .map(DomainMapper::toDomain).toList();
    }

    @Override
    public List<Pot> findAllWithArchived(Id userId) {
        return jpa.findByUserId(userId.value()).stream()
            .map(DomainMapper::toDomain).toList();
    }

    @Override
    public java.util.Optional<Pot> findById(Id id, Id userId) {
        return jpa.findByIdAndUserId(id.value(), userId.value())
            .map(DomainMapper::toDomain);
    }

    @Override
    public java.util.Optional<Pot> findDefault(Id userId) {
        return jpa.findByUserIdAndIsDefaultTrue(userId.value())
            .map(DomainMapper::toDomain);
    }

    @Override
    public List<PotSnapshot> findSnapshots(Id userId) {
        return jpa.findByUserId(userId.value()).stream()
            .map(e -> {
                Pot pot = DomainMapper.toDomain(e);
                long totalIn = potAllocJpa.sumAmountByPotId(e.getId());
                long totalOut = expenseAllocJpa.sumAmountByPotId(e.getId());
                return new PotSnapshot(pot, Money.fromCents(totalIn - totalOut));
            })
            .toList();
    }
}
