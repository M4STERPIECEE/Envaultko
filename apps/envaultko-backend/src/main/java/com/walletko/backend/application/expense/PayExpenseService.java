package com.walletko.backend.application.expense;

import com.walletko.backend.domain.expense.DrawFrom;
import com.walletko.backend.domain.expense.Expense;
import com.walletko.backend.domain.expense.ExpenseRepository;
import com.walletko.backend.domain.pot.PotRepository;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.domain.tag.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PayExpenseService {
    private final ExpenseRepository expenseRepo;
    private final PotRepository potRepo;

    public PayExpenseService(ExpenseRepository expenseRepo, PotRepository potRepo) {
        this.expenseRepo = expenseRepo;
        this.potRepo = potRepo;
    }

    @Transactional
    public Id execute(Name name, List<Tag> tags, List<DrawFrom> drawFrom,
                       Id userId, Datetime createdAt) {
        var snapshots = potRepo.findSnapshots(userId);
        var expense = createdAt != null
            ? Expense.create(name, tags, drawFrom, snapshots, userId, createdAt)
            : Expense.create(name, tags, drawFrom, snapshots, userId);
        expenseRepo.save(expense);
        return expense.id();
    }
}
