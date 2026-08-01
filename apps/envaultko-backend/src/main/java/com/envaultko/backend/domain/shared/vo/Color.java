package com.envaultko.backend.domain.shared.vo;

import java.util.Objects;

public record Color(String value) {

    public Color {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Color must not be null or empty");
        }
        value = value.trim();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Color color && value.equals(color.value);
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
