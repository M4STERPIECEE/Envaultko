package com.walletko.backend.application.pot;
import com.walletko.backend.domain.pot.PotCollection;
import com.walletko.backend.domain.pot.PotRepository;
import com.walletko.backend.domain.pot.PotUpdate;
import com.walletko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AddPotService {
    private final PotRepository potRepo;

    public AddPotService(PotRepository potRepo) {
        this.potRepo = potRepo;
    }

    @Transactional
    public Id execute(Name name, Percentage percentage, Color color,
                        List<PotUpdate> otherPots, Id userId) {
        var pots = potRepo.findAll(userId);
        var collection = new PotCollection(pots);
        var newPot = collection.addPot(name, percentage, color, otherPots, userId);
        potRepo.save(newPot);
        for (var update : otherPots) {
            collection.findById(update.id()).ifPresent(potRepo::save);
        }
        return newPot.id();
    }
}
