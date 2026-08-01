package com.envaultko.backend.domain.shared.vo;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

public record Datetime(Instant value) {

    public Datetime {
        Objects.requireNonNull(value, "Datetime value must not be null");
    }

    public static Datetime now() {
        return new Datetime(Instant.now());
    }

    public static Datetime of(Instant instant) {
        return new Datetime(instant);
    }

    public static Datetime of(Date date) {
        return new Datetime(date.toInstant());
    }

    public static Datetime of(OffsetDateTime odt) {
        return new Datetime(odt.toInstant());
    }

    public OffsetDateTime toOffsetDateTime() {
        return OffsetDateTime.ofInstant(value, ZoneOffset.UTC);
    }

    public Date toDate() {
        return Date.from(value);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Datetime datetime && value.equals(datetime.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
