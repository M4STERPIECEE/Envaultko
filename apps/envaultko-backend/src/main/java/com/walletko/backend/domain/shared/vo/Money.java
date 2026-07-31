package com.walletko.backend.domain.shared.vo;

import java.util.Objects;

public record Money(long rawCents) {

    public Money {
        if (rawCents < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative: " + rawCents);
        }
    }

    public static Money fromCents(long cents) {
        return new Money(cents);
    }

    public static Money of(double amount) {
        return new Money(Math.round(amount * 100));
    }

    public double value() {
        return rawCents / 100.0;
    }

    public Money add(Money other) {
        return new Money(this.rawCents + other.rawCents);
    }

    public Money subtract(Money other) {
        if (this.rawCents < other.rawCents) {
            throw new IllegalArgumentException(
                "Insufficient funds: " + this.rawCents + " < " + other.rawCents);
        }
        return new Money(this.rawCents - other.rawCents);
    }

    public Money percentOf(Percentage percentage) {
        long result = (long) Math.floor(this.rawCents * percentage.rate());
        return new Money(Math.max(0, result));
    }

    public boolean isLessThan(Money other) {
        return this.rawCents < other.rawCents;
    }

    public boolean isLessOrEqualThan(Money other) {
        return this.rawCents <= other.rawCents;
    }

    public boolean isZero() {
        return rawCents == 0;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Money money && rawCents == money.rawCents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawCents);
    }

    @Override
    public String toString() {
        return String.format("%.2f", value());
    }
}
