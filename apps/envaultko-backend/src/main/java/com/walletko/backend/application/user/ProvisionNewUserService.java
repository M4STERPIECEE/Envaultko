package com.walletko.backend.application.user;

import com.walletko.backend.domain.pot.*;
import com.walletko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProvisionNewUserService {
    private final PotRepository potRepo;

    public ProvisionNewUserService(PotRepository potRepo) {
        this.potRepo = potRepo;
    }

    @Transactional
    public void execute(Id userId) {
        var defaultPot = Pot.createDefault(Id.generate(), userId);
        potRepo.save(defaultPot);
    }
}
