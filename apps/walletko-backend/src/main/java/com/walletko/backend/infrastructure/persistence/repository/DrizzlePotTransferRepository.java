package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.domain.pot.PotTransfer;
import com.walletko.backend.domain.pot.PotTransferRepository;
import com.walletko.backend.infrastructure.persistence.mapper.DomainMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DrizzlePotTransferRepository implements PotTransferRepository {
    private final TransactionJpaRepository txJpa;
    private final PotAllocationJpaRepository potAllocJpa;
    private final ExpenseAllocationJpaRepository expenseAllocJpa;

    public DrizzlePotTransferRepository(TransactionJpaRepository txJpa,
                                         PotAllocationJpaRepository potAllocJpa,
                                         ExpenseAllocationJpaRepository expenseAllocJpa) {
        this.txJpa = txJpa;
        this.potAllocJpa = potAllocJpa;
        this.expenseAllocJpa = expenseAllocJpa;
    }

    @Override
    public void save(PotTransfer transfer) {
        var d = transfer.data();
        // Create the transfer transaction
        var tx = DomainMapper.toJpa("transfer", d.id(), d.name(), d.amount(),
                                     d.userId(), null, d.createdAt(), d.createdAt());
        txJpa.save(tx);

        // Deduct from source pot
        var outAlloc = new com.walletko.backend.infrastructure.persistence.entity.ExpenseAllocationEntity();
        outAlloc.setId(java.util.UUID.randomUUID().toString());
        outAlloc.setTransactionId(d.id().value());
        outAlloc.setPotId(d.fromPotId().value());
        outAlloc.setAmount(d.amount().rawCents());
        outAlloc.setCreatedAt(DomainMapper.toOdt(d.createdAt()));
        outAlloc.setUpdatedAt(DomainMapper.toOdt(d.createdAt()));
        expenseAllocJpa.save(outAlloc);

        // Add to destination pot
        var inAlloc = new com.walletko.backend.infrastructure.persistence.entity.PotAllocationEntity();
        inAlloc.setId(java.util.UUID.randomUUID().toString());
        inAlloc.setTransactionId(d.id().value());
        inAlloc.setPotId(d.toPotId().value());
        inAlloc.setAmount(d.amount().rawCents());
        inAlloc.setCreatedAt(DomainMapper.toOdt(d.createdAt()));
        inAlloc.setUpdatedAt(DomainMapper.toOdt(d.createdAt()));
        potAllocJpa.save(inAlloc);
    }
}
