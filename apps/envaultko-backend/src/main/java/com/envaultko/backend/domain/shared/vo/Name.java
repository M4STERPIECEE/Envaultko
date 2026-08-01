package com.envaultko.backend.domain.shared.vo;

import java.util.Objects;

public record Name(String value) {

    public Name {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }
        value = value.trim();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Name name && value.equals(name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
