package com.walletko.backend.domain.repository;

import com.walletko.backend.domain.entity.ExpenseAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseAllocationRepository extends JpaRepository<ExpenseAllocation, String> {

    List<ExpenseAllocation> findByPotId(String potId);

    List<ExpenseAllocation> findByTransactionId(String transactionId);

    @Query("SELECT COALESCE(SUM(ea.amount), 0) FROM ExpenseAllocation ea WHERE ea.potId = :potId")
    Long sumAmountByPotId(String potId);
}
