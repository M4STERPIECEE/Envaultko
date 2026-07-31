package com.walletko.backend.application.pot;

import com.walletko.backend.domain.pot.PotCollection;
import com.walletko.backend.domain.pot.PotRepository;
import com.walletko.backend.domain.pot.PotUpdate;


import com.walletko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EditAllocationService {
    private final PotRepository potRepo;

    public EditAllocationService(PotRepository potRepo) {
        this.potRepo = potRepo;
    }

    @Transactional
    public void execute(List<PotUpdate> allPots, Id userId) {
        var pots = potRepo.findAll(userId);
        var collection = new PotCollection(pots);
        collection.adjustRepartition(allPots);
        for (var update : allPots) {
            collection.findById(update.id()).ifPresent(potRepo::save);
        }
    }
}
