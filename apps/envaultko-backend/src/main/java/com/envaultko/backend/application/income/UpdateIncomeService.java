package com.envaultko.backend.application.income;

import com.envaultko.backend.domain.income.IncomeRepository;
import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.tag.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UpdateIncomeService {
    private final IncomeRepository incomeRepo;

    public UpdateIncomeService(IncomeRepository incomeRepo) {
        this.incomeRepo = incomeRepo;
    }

    @Transactional
    public Id execute(Id id, Name name, Datetime date, List<Tag> tags, Id userId) {
        var income = incomeRepo.findOne(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Income not found"));
        income.update(name, date, tags);
        incomeRepo.update(income);
        return id;
    }
}
