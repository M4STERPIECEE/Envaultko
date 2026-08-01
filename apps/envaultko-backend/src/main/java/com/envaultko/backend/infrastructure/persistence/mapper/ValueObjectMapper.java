package com.envaultko.backend.infrastructure.persistence.mapper;

import com.envaultko.backend.domain.shared.vo.*;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class ValueObjectMapper {

    public OffsetDateTime toOdt(Datetime dt) {
        return dt != null ? OffsetDateTime.ofInstant(dt.value(), ZoneOffset.UTC) : null;
    }

    public Datetime toDt(OffsetDateTime odt) {
        return odt != null ? Datetime.of(odt.toInstant()) : null;
    }

    public String idToString(Id id) {
        return id != null ? id.value() : null;
    }

    public Id stringToId(String val) {
        return val != null ? new Id(val) : null;
    }

    public String nameToString(Name name) {
        return name != null ? name.value() : null;
    }

    public Name stringToName(String val) {
        return val != null ? new Name(val) : null;
    }

    public Integer percentageToInt(Percentage p) {
        return p != null ? p.value() : null;
    }

    public Percentage intToPercentage(Integer val) {
        return val != null ? new Percentage(val) : null;
    }

    public String colorToString(Color c) {
        return c != null ? c.value() : null;
    }

    public Color stringToColor(String val) {
        return val != null ? new Color(val) : null;
    }

    public Long moneyToLong(Money m) {
        return m != null ? m.rawCents() : null;
    }

    public Money longToMoney(Long val) {
        return val != null ? Money.fromCents(val) : null;
    }
}
