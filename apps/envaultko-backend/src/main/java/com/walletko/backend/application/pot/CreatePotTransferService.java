package com.walletko.backend.application.pot;

import com.walletko.backend.domain.pot.*;
import com.walletko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreatePotTransferService {
    private final PotRepository potRepo;
    private final PotTransferRepository transferRepo;

    public CreatePotTransferService(PotRepository potRepo, PotTransferRepository transferRepo) {
        this.potRepo = potRepo;
        this.transferRepo = transferRepo;
    }

    @Transactional
    public void execute(Id fromPotId, Id toPotId, Money amount, Id userId) {
        if (fromPotId.equals(toPotId)) {
            throw new IllegalArgumentException("Source and destination pots must differ");
        }
        if (amount.isZero()) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        var snapshots = potRepo.findSnapshots(userId);
        var fromBalance = snapshots.stream()
            .filter(s -> s.pot().id().equals(fromPotId))
            .findFirst()
            .map(PotSnapshot::balance)
            .orElseThrow(() -> new IllegalArgumentException("Source pot not found"));

        if (fromBalance.isLessThan(amount)) {
            throw new IllegalArgumentException(
                "Insufficient balance in source pot (available: "
                + fromBalance.rawCents() + ", required: " + amount.rawCents() + ")");
        }

        var transfer = PotTransfer.create(fromPotId, toPotId, amount, userId);
        transferRepo.save(transfer);
    }
}
