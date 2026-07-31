package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.infrastructure.persistence.entity.ExpenseAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseAllocationJpaRepository extends JpaRepository<ExpenseAllocationEntity, String> {
    List<ExpenseAllocationEntity> findByTransactionId(String transactionId);
    List<ExpenseAllocationEntity> findByPotId(String potId);
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseAllocationEntity e WHERE e.potId = :potId")
    long sumAmountByPotId(String potId);
    void deleteByTransactionId(String transactionId);
}
