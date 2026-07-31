package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.infrastructure.persistence.entity.PotAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PotAllocationJpaRepository extends JpaRepository<PotAllocationEntity, String> {
    List<PotAllocationEntity> findByTransactionId(String transactionId);
    List<PotAllocationEntity> findByPotId(String potId);
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PotAllocationEntity p WHERE p.potId = :potId")
    long sumAmountByPotId(String potId);
    void deleteByTransactionId(String transactionId);
}
