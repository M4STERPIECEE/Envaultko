package com.walletko.backend.domain.income;

import com.walletko.backend.domain.shared.vo.Id;
import java.util.Optional;

public interface IncomeRepository {
    void save(Income income);
    void update(Income income);
    Optional<Income> findOne(Id id, Id userId);
    void markCanceled(Id id, Id userId);
}
