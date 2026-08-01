package com.envaultko.backend.domain.shared.vo;

import java.util.Objects;
import java.util.UUID;

public record Id(String value) {
    public Id {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Id must not be null or blank");
        }
    }

    public static Id generate() {
        return new Id(UUID.randomUUID().toString());
    }

    public boolean isEqual(Id other) {
        return this.value.equals(other.value);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Id id && value.equals(id.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
