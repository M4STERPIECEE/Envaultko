package com.envaultko.backend.domain.shared.vo;

import java.util.Objects;

public record Percentage(int value) {

    public Percentage {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100, got: " + value);
        }
    }

    public double rate() {
        return value / 100.0;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Percentage that && value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + "%";
    }
}
