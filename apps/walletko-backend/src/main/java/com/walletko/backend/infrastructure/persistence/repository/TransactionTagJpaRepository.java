package com.walletko.backend.infrastructure.persistence.repository;

import com.walletko.backend.infrastructure.persistence.entity.TransactionTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionTagJpaRepository extends JpaRepository<TransactionTagEntity, TransactionTagEntity.TransactionTagId> {
    List<TransactionTagEntity> findByTransactionId(String transactionId);
    void deleteByTransactionId(String transactionId);
}
