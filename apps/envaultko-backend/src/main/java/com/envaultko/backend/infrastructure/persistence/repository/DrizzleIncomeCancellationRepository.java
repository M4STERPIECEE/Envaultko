package com.envaultko.backend.infrastructure.persistence.repository;

import com.envaultko.backend.domain.income.CancellationData;
import com.envaultko.backend.domain.income.IncomeCancellation;
import com.envaultko.backend.domain.income.IncomeCancellationRepository;
import com.envaultko.backend.domain.shared.vo.Id;
import com.envaultko.backend.domain.shared.vo.Name;
import com.envaultko.backend.infrastructure.persistence.mapper.DomainMapper;
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
        txJpa.save(DomainMapper.toJpa(d));

        for (var line : d.lines()) {
            expenseAllocJpa.save(DomainMapper.expenseAllocation(d.id(), line.potId(), line.amount(), d.createdAt()));
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
