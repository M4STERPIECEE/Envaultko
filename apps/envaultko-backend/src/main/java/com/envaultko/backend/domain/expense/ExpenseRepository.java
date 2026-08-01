package com.envaultko.backend.domain.expense;

import com.envaultko.backend.domain.shared.vo.Id;
import java.util.Optional;

public interface ExpenseRepository {
    void save(Expense expense);
    void update(Expense expense);
    Optional<Expense> findOne(Id id, Id userId);
    void markCanceled(Id id, Id userId);
}
