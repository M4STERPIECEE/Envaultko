package com.walletko.backend.domain.expense;

import com.walletko.backend.domain.shared.vo.Id;
import java.util.Optional;

public interface ExpenseRepository {
    void save(Expense expense);
    void update(Expense expense);
    Optional<Expense> findOne(Id id, Id userId);
    void markCanceled(Id id, Id userId);
}
