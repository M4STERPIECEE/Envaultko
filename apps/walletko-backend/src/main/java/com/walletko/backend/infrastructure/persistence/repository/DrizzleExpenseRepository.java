package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.domain.expense.*;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.infrastructure.persistence.mapper.DomainMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class DrizzleExpenseRepository implements ExpenseRepository {
    private final TransactionJpaRepository txJpa;
    private final ExpenseAllocationJpaRepository allocJpa;
    private final TransactionTagJpaRepository txTagJpa;
    private final TagJpaRepository tagJpa;

    public DrizzleExpenseRepository(TransactionJpaRepository txJpa,
                                     ExpenseAllocationJpaRepository allocJpa,
                                     TransactionTagJpaRepository txTagJpa,
                                     TagJpaRepository tagJpa) {
        this.txJpa = txJpa;
        this.allocJpa = allocJpa;
        this.txTagJpa = txTagJpa;
        this.tagJpa = tagJpa;
    }

    @Override
    public void save(Expense expense) {
        var d = expense.data();
        txJpa.save(DomainMapper.toJpa(d));
        for (var alloc : d.allocations()) {
            allocJpa.save(DomainMapper.toJpa(alloc));
        }
        for (var tag : d.tags()) {
            txTagJpa.save(DomainMapper.toJpa(d.id().value(), tag.data().id().value()));
        }
    }

    @Override
    public void update(Expense expense) {
        var d = expense.data();
        txJpa.findByIdAndUserId(d.id().value(), d.userId().value()).ifPresent(tx -> {
            tx.setName(d.name().value());
            tx.setUpdatedAt(DomainMapper.toOdt(Datetime.now()));
            txJpa.save(tx);
        });
        txTagJpa.deleteByTransactionId(d.id().value());
        for (var tag : d.tags()) {
            txTagJpa.save(DomainMapper.toJpa(d.id().value(), tag.data().id().value()));
        }
    }

    @Override
    public Optional<Expense> findOne(Id id, Id userId) {
        return txJpa.findByIdAndUserId(id.value(), userId.value())
            .filter(tx -> "expense".equals(tx.getType()))
            .map(tx -> {
                var tags = txTagJpa.findByTransactionId(tx.getId()).stream()
                    .map(tt -> tagJpa.findById(tt.getTagId()))
                    .filter(Optional::isPresent)
                    .map(opt -> DomainMapper.toDomain(opt.get()))
                    .toList();
                var allocs = allocJpa.findByTransactionId(tx.getId()).stream()
                    .map(e -> DomainMapper.toDomain(e, new Id(tx.getId())))
                    .toList();
                return new Expense(
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
            .ifPresent(txJpa::delete);
    }
}
