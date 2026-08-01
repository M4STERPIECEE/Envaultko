package com.envaultko.backend.infrastructure.persistence.repository;

import com.envaultko.backend.domain.pot.PotTransfer;
import com.envaultko.backend.domain.pot.PotTransferRepository;
import com.envaultko.backend.infrastructure.persistence.mapper.DomainMapper;
import org.springframework.stereotype.Repository;

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
        txJpa.save(DomainMapper.toJpa(d));
        expenseAllocJpa.save(DomainMapper.expenseAllocation(d.id(), d.fromPotId(), d.amount(), d.createdAt()));
        potAllocJpa.save(DomainMapper.potAllocation(d.id(), d.toPotId(), d.amount(), d.createdAt()));
    }
}
