package com.envaultko.backend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pot_allocations")
@Getter @Setter @NoArgsConstructor
public class PotAllocationEntity {
    @Id
    private String id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "pot_id", nullable = false)
    private String potId;

    @Column(nullable = false)
    private long amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
