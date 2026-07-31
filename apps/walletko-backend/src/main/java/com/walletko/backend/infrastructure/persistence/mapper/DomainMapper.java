package com.walletko.backend.infrastructure.persistence.mapper;

import com.walletko.backend.domain.expense.*;
import com.walletko.backend.domain.income.*;
import com.walletko.backend.domain.pot.*;
import com.walletko.backend.domain.savedview.SavedView;
import com.walletko.backend.domain.shared.vo.*;
import com.walletko.backend.domain.tag.Tag;
import com.walletko.backend.infrastructure.persistence.entity.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

public class DomainMapper {

    // ── Timestamp helpers ──
    public static OffsetDateTime toOdt(Datetime dt) {
        return dt != null ? OffsetDateTime.ofInstant(dt.value(), ZoneOffset.UTC) : null;
    }

    public static Datetime toDt(OffsetDateTime odt) {
        return odt != null ? Datetime.of(odt.toInstant()) : null;
    }

    // ── Pot ──
    public static PotEntity toJpa(Pot domain) {
        var d = domain.data();
        var e = new PotEntity();
        e.setId(d.id().value());
        e.setName(d.name().value());
        e.setPercentage(d.percentage().value());
        e.setColor(d.color().value());
        e.setDefault(d.isDefault());
        e.setUserId(d.userId().value());
        e.setCreatedAt(toOdt(d.createdAt()));
        e.setUpdatedAt(toOdt(d.updatedAt()));
        e.setArchivedAt(toOdt(d.archivedAt()));
        return e;
    }

    public static Pot toDomain(PotEntity e) {
        return new Pot(
            new Id(e.getId()), new Name(e.getName()),
            new Percentage(e.getPercentage()), new Color(e.getColor()),
            e.isDefault(), new Id(e.getUserId()),
            toDt(e.getCreatedAt()), toDt(e.getUpdatedAt()), toDt(e.getArchivedAt())
        );
    }

    // ── Tag ──
    public static TagEntity toJpa(Tag domain) {
        var d = domain.data();
        var e = new TagEntity();
        e.setId(d.id().value());
        e.setName(d.name().value());
        e.setUserId(d.userId().value());
        e.setCreatedAt(toOdt(d.createdAt()));
        return e;
    }

    public static Tag toDomain(TagEntity e) {
        return new Tag(new Id(e.getId()), new Name(e.getName()),
                       new Id(e.getUserId()), toDt(e.getCreatedAt()), null);
    }

    // ── Transaction ──
    public static TransactionEntity toJpa(String type, Id id, Name name, Money amount,
                                           Id userId, String cancelsTransactionId,
                                           Datetime createdAt, Datetime updatedAt) {
        var e = new TransactionEntity();
        e.setId(id.value());
        e.setType(type);
        e.setName(name.value());
        e.setAmount(amount.rawCents());
        e.setUserId(userId.value());
        e.setCancelsTransactionId(cancelsTransactionId);
        e.setCreatedAt(toOdt(createdAt));
        e.setUpdatedAt(toOdt(updatedAt != null ? updatedAt : createdAt));
        return e;
    }

    // ── PotAllocation (income) ──
    public static PotAllocationEntity toJpa(PotAllocation domain) {
        var d = domain.data();
        var e = new PotAllocationEntity();
        e.setId(d.id().value());
        e.setTransactionId(d.incomeId().value());
        e.setPotId(d.potId().value());
        e.setAmount(d.amount().rawCents());
        e.setCreatedAt(toOdt(d.createdAt()));
        e.setUpdatedAt(toOdt(d.updatedAt()));
        return e;
    }

    public static PotAllocation toDomain(PotAllocationEntity e, Id incomeId) {
        return new PotAllocation(
            new Id(e.getId()), new Id(e.getPotId()), incomeId,
            Money.fromCents(e.getAmount()), toDt(e.getCreatedAt()), toDt(e.getUpdatedAt())
        );
    }

    // ── ExpenseAllocation ──
    public static ExpenseAllocationEntity toJpa(ExpenseAllocation domain) {
        var d = domain.data();
        var e = new ExpenseAllocationEntity();
        e.setId(d.id().value());
        e.setTransactionId(d.expenseId().value());
        e.setPotId(d.potId().value());
        e.setAmount(d.amount().rawCents());
        e.setCreatedAt(toOdt(d.createdAt()));
        e.setUpdatedAt(toOdt(d.updatedAt()));
        return e;
    }

    public static ExpenseAllocation toDomain(ExpenseAllocationEntity e, Id expenseId) {
        return new ExpenseAllocation(
            new Id(e.getId()), new Id(e.getPotId()), expenseId,
            Money.fromCents(e.getAmount()), toDt(e.getCreatedAt()), toDt(e.getUpdatedAt())
        );
    }

    // ── TransactionTag ──
    public static TransactionTagEntity toJpa(String transactionId, String tagId) {
        return new TransactionTagEntity(transactionId, tagId);
    }

    // ── SavedView ──
    public static SavedViewEntity toJpa(SavedView domain) {
        var d = domain.data();
        var e = new SavedViewEntity();
        e.setId(d.id().value());
        e.setUserId(d.userId().value());
        e.setName(d.name().value());
        e.setDescription(d.description());
        e.setNameFilter(d.nameFilter());
        e.setTagIds(toPgArray(d.tagIds()));
        e.setCreatedAt(toOdt(d.createdAt()));
        e.setUpdatedAt(toOdt(d.updatedAt()));
        return e;
    }

    public static SavedView toDomain(SavedViewEntity e) {
        return new SavedView(
            new Id(e.getId()), new Id(e.getUserId()),
            new Name(e.getName()), e.getDescription(), e.getNameFilter(),
            fromPgArray(e.getTagIds()), toDt(e.getCreatedAt()), toDt(e.getUpdatedAt())
        );
    }

    private static String toPgArray(List<Id> ids) {
        if (ids == null || ids.isEmpty()) return "{}";
        return ids.stream().map(id -> "\"" + id.value() + "\"")
                  .collect(Collectors.joining(",", "{", "}"));
    }

    private static List<Id> fromPgArray(String pgArray) {
        if (pgArray == null || pgArray.equals("{}") || pgArray.isBlank()) return List.of();
        String trimmed = pgArray.substring(1, pgArray.length() - 1);
        return Arrays.stream(trimmed.split(","))
            .map(s -> s.replace("\"", "").trim())
            .filter(s -> !s.isEmpty())
            .map(Id::new)
            .toList();
    }
}
