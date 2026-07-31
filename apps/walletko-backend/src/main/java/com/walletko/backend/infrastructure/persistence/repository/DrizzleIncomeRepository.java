package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.domain.income.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.domain.tag.Tag;
import com.walletko.backend.infrastructure.persistence.mapper.DomainMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class DrizzleIncomeRepository implements IncomeRepository {
    private final TransactionJpaRepository txJpa;
    private final PotAllocationJpaRepository allocJpa;
    private final TransactionTagJpaRepository txTagJpa;
    private final TagJpaRepository tagJpa;

    public DrizzleIncomeRepository(TransactionJpaRepository txJpa,
                                    PotAllocationJpaRepository allocJpa,
                                    TransactionTagJpaRepository txTagJpa,
                                    TagJpaRepository tagJpa) {
        this.txJpa = txJpa;
        this.allocJpa = allocJpa;
        this.txTagJpa = txTagJpa;
        this.tagJpa = tagJpa;
    }

    @Override
    public void save(Income income) {
        var d = income.data();
        // Save transaction row
        txJpa.save(DomainMapper.toJpa("income", d.id(), d.name(), d.amount(),
                                       d.userId(), null, d.createdAt(), d.createdAt()));
        // Save pot allocations
        for (var alloc : d.allocations()) {
            allocJpa.save(DomainMapper.toJpa(alloc));
        }
        // Save tag links
        for (var tag : d.tags()) {
            txTagJpa.save(DomainMapper.toJpa(d.id().value(), tag.data().id().value()));
        }
    }

    @Override
    public void update(Income income) {
        var d = income.data();
        var txOpt = txJpa.findByIdAndUserId(d.id().value(), d.userId().value());
        txOpt.ifPresent(tx -> {
            tx.setName(d.name().value());
            tx.setUpdatedAt(DomainMapper.toOdt(Datetime.now()));
            txJpa.save(tx);
        });
        // Re-sync tag links
        txTagJpa.deleteByTransactionId(d.id().value());
        for (var tag : d.tags()) {
            txTagJpa.save(DomainMapper.toJpa(d.id().value(), tag.data().id().value()));
        }
    }

    @Override
    public Optional<Income> findOne(Id id, Id userId) {
        return txJpa.findByIdAndUserId(id.value(), userId.value())
            .filter(tx -> "income".equals(tx.getType()))
            .map(tx -> {
                var tags = txTagJpa.findByTransactionId(tx.getId()).stream()
                    .map(tt -> tagJpa.findById(tt.getTagId()))
                    .filter(Optional::isPresent)
                    .map(opt -> DomainMapper.toDomain(opt.get()))
                    .toList();
                var allocs = allocJpa.findByTransactionId(tx.getId()).stream()
                    .map(e -> DomainMapper.toDomain(e, new Id(tx.getId())))
                    .toList();
                return new Income(
                    new Id(tx.getId()), new Name(tx.getName()),
                    Money.fromCents(tx.getAmount()), new Id(tx.getUserId()),
                    DomainMapper.toDt(tx.getCreatedAt()),
                    DomainMapper.toDt(tx.getUpdatedAt()),
                    tags, allocs
                );
            });
    }

    @Override
    public void markCanceled(Id id, Id userId) {
        txJpa.findByIdAndUserId(id.value(), userId.value())
            .ifPresent(tx -> {
                txJpa.delete(tx); // replaced by cancellation txns
            });
    }
}
