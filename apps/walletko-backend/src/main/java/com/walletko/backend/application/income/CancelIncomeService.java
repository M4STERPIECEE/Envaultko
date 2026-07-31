package com.walletko.backend.application.income;

import com.walletko.backend.domain.income.BlockingPot;
import com.walletko.backend.domain.income.CancelIncomeBlockedError;
import com.walletko.backend.domain.income.Income;
import com.walletko.backend.domain.income.IncomeCancellation;
import com.walletko.backend.domain.income.IncomeCancellationRepository;
import com.walletko.backend.domain.income.IncomeRepository;
import com.walletko.backend.domain.pot.PotRepository;
import com.walletko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CancelIncomeService {
    private final IncomeRepository incomeRepo;
    private final PotRepository potRepo;
    private final IncomeCancellationRepository cancellationRepo;

    public CancelIncomeService(IncomeRepository incomeRepo,
                                PotRepository potRepo,
                                IncomeCancellationRepository cancellationRepo) {
        this.incomeRepo = incomeRepo;
        this.potRepo = potRepo;
        this.cancellationRepo = cancellationRepo;
    }

    @Transactional
    public void execute(Id incomeId, Id userId) {
        var income = incomeRepo.findOne(incomeId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Income not found"));

        var snapshots = potRepo.findSnapshots(userId);
        List<BlockingPot> blocked = new ArrayList<>();

        for (var alloc : income.allocations()) {
            var snap = snapshots.stream()
                .filter(s -> s.pot().id().equals(alloc.potId()))
                .findFirst();
            if (snap.isPresent() && snap.get().balance().isLessThan(alloc.amount())) {
                long shortfall = alloc.amount().rawCents() - snap.get().balance().rawCents();
                blocked.add(new BlockingPot(
                    snap.get().pot().name().value(), shortfall));
            }
        }

        if (!blocked.isEmpty()) {
            throw new CancelIncomeBlockedError(blocked);
        }

        var cancellation = IncomeCancellation.fromIncome(income);
        cancellationRepo.save(cancellation);
        incomeRepo.markCanceled(incomeId, userId);
    }
}
