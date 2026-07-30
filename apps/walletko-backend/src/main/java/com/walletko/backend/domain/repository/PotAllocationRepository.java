package com.walletko.backend.domain.repository;

import com.walletko.backend.domain.entity.PotAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PotAllocationRepository extends JpaRepository<PotAllocation, String> {

    List<PotAllocation> findByPotId(String potId);

    List<PotAllocation> findByTransactionId(String transactionId);

    @Query("SELECT COALESCE(SUM(pa.amount), 0) FROM PotAllocation pa WHERE pa.potId = :potId")
    Long sumAmountByPotId(String potId);
}
