package com.walletko.backend.domain.entity;

import com.walletko.backend.domain.enums.TransactionType;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "cancels_transaction_id")
    private String cancelsTransactionId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public Transaction() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCancelsTransactionId() { return cancelsTransactionId; }
    public void setCancelsTransactionId(String cancelsTransactionId) { this.cancelsTransactionId = cancelsTransactionId; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
