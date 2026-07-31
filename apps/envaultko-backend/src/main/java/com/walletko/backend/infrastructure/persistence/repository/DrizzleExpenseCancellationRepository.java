package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.domain.expense.CancellationData;
import com.walletko.backend.domain.expense.ExpenseCancellation;
import com.walletko.backend.domain.expense.ExpenseCancellationRepository;
import com.walletko.backend.domain.shared.vo.Id;
import com.walletko.backend.domain.shared.vo.Name;
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
        txJpa.save(DomainMapper.toJpa(d));

        for (var line : d.lines()) {
            potAllocJpa.save(DomainMapper.potAllocation(d.id(), line.potId(), line.amount(), d.createdAt()));
        }

        var markData = new CancellationData(
            Id.generate(),
            d.cancelsTransactionId(),
            new Name("Canceled: " + d.name().value()),
            d.amount(),
            d.userId(),
            d.lines(),
            d.createdAt()
        );
        txJpa.save(DomainMapper.toJpa(markData));
    }
}
