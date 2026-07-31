package com.walletko.backend.application.expense;

import com.walletko.backend.domain.expense.ExpenseRepository;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.domain.tag.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UpdateExpenseService {
    private final ExpenseRepository expenseRepo;

    public UpdateExpenseService(ExpenseRepository expenseRepo) {
        this.expenseRepo = expenseRepo;
    }

    @Transactional
    public Id execute(Id id, Name name, Datetime date, List<Tag> tags, Id userId) {
        var expense = expenseRepo.findOne(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        expense.update(name, date, tags);
        expenseRepo.update(expense);
        return id;
    }
}
