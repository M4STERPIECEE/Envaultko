package com.envaultko.backend.application.pot;

import com.envaultko.backend.domain.pot.PotRepository;
import com.envaultko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditPotService {
    private final PotRepository potRepo;

    public EditPotService(PotRepository potRepo) {
        this.potRepo = potRepo;
    }

    @Transactional
    public void execute(Id potId, Name name, Color color, Id userId) {
        var pot = potRepo.findById(potId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Pot not found"));
        pot.changeName(name);
        pot.changeColor(color);
        potRepo.save(pot);
    }
}
