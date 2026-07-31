package com.walletko.backend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
@Getter @Setter @NoArgsConstructor
public class TransactionEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long amount;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "cancels_transaction_id")
    private String cancelsTransactionId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
