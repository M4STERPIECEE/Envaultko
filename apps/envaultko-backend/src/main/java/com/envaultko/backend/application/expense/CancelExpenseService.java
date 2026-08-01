package com.envaultko.backend.application.expense;

import com.envaultko.backend.domain.expense.*;
import com.envaultko.backend.domain.pot.Pot;
import com.envaultko.backend.domain.pot.PotRepository;
import com.envaultko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.function.Function;

@Service
public class CancelExpenseService {
    private final ExpenseRepository expenseRepo;
    private final PotRepository potRepo;
    private final ExpenseCancellationRepository cancellationRepo;

    public CancelExpenseService(ExpenseRepository expenseRepo,
                                 PotRepository potRepo,
                                 ExpenseCancellationRepository cancellationRepo) {
        this.expenseRepo = expenseRepo;
        this.potRepo = potRepo;
        this.cancellationRepo = cancellationRepo;
    }

    @Transactional
    public void execute(Id expenseId, Id userId) {
        var expense = expenseRepo.findOne(expenseId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        var allPots = potRepo.findAllWithArchived(userId);
        var defaultPot = allPots.stream().filter(p -> p.isDefault()).findFirst()
            .orElseThrow(() -> new IllegalStateException("No default pot found"));

        Function<Id, Id> resolvePotId = (archivedPotId) -> {
            var archived = allPots.stream()
                .filter(p -> p.id().equals(archivedPotId))
                .findFirst();
            return archived.filter(Pot::isArchived)
                .map(p -> defaultPot.id())
                .orElse(archivedPotId);
        };

        var cancellation = ExpenseCancellation.fromExpense(expense, resolvePotId);
        cancellationRepo.save(cancellation);
        expenseRepo.markCanceled(expenseId, userId);
    }
}
