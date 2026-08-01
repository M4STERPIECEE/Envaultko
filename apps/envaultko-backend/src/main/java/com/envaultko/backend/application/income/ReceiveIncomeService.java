package com.envaultko.backend.application.income;

import com.envaultko.backend.application.tag.ResolveOwnedTags.TagInput;
import com.envaultko.backend.domain.income.*;
import com.envaultko.backend.domain.pot.PotRepository;
import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.tag.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ReceiveIncomeService {
    private final IncomeRepository incomeRepo;
    private final PotRepository potRepo;

    public ReceiveIncomeService(IncomeRepository incomeRepo, PotRepository potRepo) {
        this.incomeRepo = incomeRepo;
        this.potRepo = potRepo;
    }

    @Transactional
    public Id execute(Name name, Money amount, List<Tag> tags, Id userId, Datetime createdAt) {
        var pots = potRepo.findAll(userId);
        var income = createdAt != null
            ? Income.create(name, amount, tags, pots, userId, createdAt)
            : Income.create(name, amount, tags, pots, userId);
        incomeRepo.save(income);
        return income.id();
    }
}
