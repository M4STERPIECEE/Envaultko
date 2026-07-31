package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.domain.expense.*;
import com.walletko.backend.infrastructure.persistence.mapper.DomainMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DrizzleExpenseCancellationRepository implements ExpenseCancellationRepository {
    private final TransactionJpaRepository txJpa;
    private final PotAllocationJpaRepository potAllocJpa;

    public DrizzleExpenseCancellationRepository(TransactionJpaRepository txJpa,
                                                 PotAllocationJpaRepository potAllocJpa) {
        this.txJpa = txJpa;
        this.potAllocJpa = potAllocJpa;
    }

    @Override
    public void save(ExpenseCancellation cancellation) {
        var d = cancellation.data();
        // Create the expense_cancellation transaction (money returning to pots)
        var tx = DomainMapper.toJpa("expense_cancellation", d.id(), d.name(),
                                     d.amount(), d.userId(),
                                     d.cancelsTransactionId().value(),
                                     d.createdAt(), d.createdAt());
        txJpa.save(tx);

        // Create pot allocations for each line (money returning to pots)
        for (var line : d.lines()) {
            var alloc = new com.walletko.backend.infrastructure.persistence.entity.PotAllocationEntity();
            alloc.setId(java.util.UUID.randomUUID().toString());
            alloc.setTransactionId(d.id().value());
            alloc.setPotId(line.potId().value());
            alloc.setAmount(line.amount().rawCents());
            alloc.setCreatedAt(DomainMapper.toOdt(d.createdAt()));
            alloc.setUpdatedAt(DomainMapper.toOdt(d.createdAt()));
            potAllocJpa.save(alloc);
        }

        // Create canceled_expense marker
        var markTx = DomainMapper.toJpa("canceled_expense", com.walletko.backend.domain.shared.vo.Id.generate(),
            new com.walletko.backend.domain.shared.vo.Name("Canceled: " + d.name().value()),
            d.amount(), d.userId(), d.cancelsTransactionId().value(),
            d.createdAt(), d.createdAt());
        txJpa.save(markTx);
    }
}
