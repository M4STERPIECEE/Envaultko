package com.envaultko.backend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "transaction_tags")
@IdClass(TransactionTagEntity.TransactionTagId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TransactionTagEntity {

    @Id
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Id
    @Column(name = "tag_id", nullable = false)
    private String tagId;

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class TransactionTagId implements Serializable {
        private String transactionId;
        private String tagId;
    }
}
