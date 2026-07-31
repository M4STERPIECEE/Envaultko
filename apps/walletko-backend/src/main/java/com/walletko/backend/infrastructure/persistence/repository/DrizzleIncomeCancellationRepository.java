package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.domain.income.*;
import com.walletko.backend.infrastructure.persistence.mapper.DomainMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DrizzleIncomeCancellationRepository implements IncomeCancellationRepository {
    private final TransactionJpaRepository txJpa;
    private final ExpenseAllocationJpaRepository expenseAllocJpa;

    public DrizzleIncomeCancellationRepository(TransactionJpaRepository txJpa,
                                                ExpenseAllocationJpaRepository expenseAllocJpa) {
        this.txJpa = txJpa;
        this.expenseAllocJpa = expenseAllocJpa;
    }

    @Override
    public void save(IncomeCancellation cancellation) {
        var d = cancellation.data();
        // Create the income_cancellation transaction (negative impact on balance)
        var tx = DomainMapper.toJpa("income_cancellation", d.id(), d.name(),
                                     d.amount(), d.userId(),
                                     d.cancelsTransactionId().value(),
                                     d.createdAt(), d.createdAt());
        txJpa.save(tx);

        // Create expense allocations for each line (money leaving pots)
        for (var line : d.lines()) {
            var alloc = new com.walletko.backend.infrastructure.persistence.entity.ExpenseAllocationEntity();
            alloc.setId(java.util.UUID.randomUUID().toString());
            alloc.setTransactionId(d.id().value());
            alloc.setPotId(line.potId().value());
            alloc.setAmount(line.amount().rawCents());
            alloc.setCreatedAt(DomainMapper.toOdt(d.createdAt()));
            alloc.setUpdatedAt(DomainMapper.toOdt(d.createdAt()));
            expenseAllocJpa.save(alloc);
        }

        // Create canceled_income marker
        var markTx = DomainMapper.toJpa("canceled_income", com.walletko.backend.domain.shared.vo.Id.generate(),
            new com.walletko.backend.domain.shared.vo.Name("Canceled: " + d.name().value()),
            d.amount(), d.userId(), d.cancelsTransactionId().value(),
            d.createdAt(), d.createdAt());
        txJpa.save(markTx);
    }
}
