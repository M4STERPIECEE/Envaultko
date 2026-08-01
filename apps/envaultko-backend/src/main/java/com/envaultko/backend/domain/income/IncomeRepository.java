package com.envaultko.backend.domain.income;

import com.envaultko.backend.domain.shared.vo.Id;
import java.util.Optional;

public interface IncomeRepository {
    void save(Income income);
    void update(Income income);
    Optional<Income> findOne(Id id, Id userId);
    void markCanceled(Id id, Id userId);
}
