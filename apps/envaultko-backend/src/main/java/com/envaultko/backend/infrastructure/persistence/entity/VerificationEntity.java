package com.envaultko.backend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(name = "verification")
@Getter @Setter @NoArgsConstructor
public class VerificationEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String identifier;

    @Column(nullable = false)
    private String value;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
