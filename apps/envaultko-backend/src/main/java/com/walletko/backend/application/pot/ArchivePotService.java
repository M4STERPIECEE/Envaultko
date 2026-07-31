package com.walletko.backend.application.pot;

import com.walletko.backend.domain.pot.PotCollection;
import com.walletko.backend.domain.pot.PotRepository;
import com.walletko.backend.domain.pot.PotSnapshot;
import com.walletko.backend.domain.pot.PotTransfer;
import com.walletko.backend.domain.pot.PotTransferRepository;
import com.walletko.backend.domain.pot.PotUpdate;
import com.walletko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ArchivePotService {
    private final PotRepository potRepo;
    private final PotTransferRepository transferRepo;

    public ArchivePotService(PotRepository potRepo, PotTransferRepository transferRepo) {
        this.potRepo = potRepo;
        this.transferRepo = transferRepo;
    }

    @Transactional
    public void execute(Id potId, Id toPotId, List<PotUpdate> remaining, Id userId) {
        var snapshots = potRepo.findSnapshots(userId);
        var balance = snapshots.stream()
            .filter(s -> s.pot().id().equals(potId))
            .findFirst()
            .map(PotSnapshot::balance)
            .orElse(Money.fromCents(0));

        if (!balance.isZero()) {
            if (toPotId == null) {
                throw new IllegalArgumentException(
                    "Pot has balance of " + balance.rawCents()
                    + " cents; provide toPotId to transfer balance");
            }
            var transfer = PotTransfer.create(potId, toPotId, balance, userId);
            transferRepo.save(transfer);
        }

        var pots = potRepo.findAll(userId);
        var collection = new PotCollection(pots);
        collection.archivePot(potId, remaining);
        for (var update : remaining) {
            collection.findById(update.id()).ifPresent(potRepo::save);
        }
    }
}
