package com.envaultko.backend.infrastructure.persistence.mapper;

import com.envaultko.backend.domain.expense.*;
import com.envaultko.backend.domain.income.*;
import com.envaultko.backend.domain.pot.*;
import com.envaultko.backend.domain.savedview.SavedView;
import com.envaultko.backend.domain.shared.vo.*;
import com.envaultko.backend.domain.tag.Tag;
import com.envaultko.backend.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.OffsetDateTime;

@Component
public class DomainMapper {

    private static PotMapper potMapperStatic;
    private static TagMapper tagMapperStatic;
    private static SavedViewMapper savedViewMapperStatic;
    private static TransactionMapper transactionMapperStatic;
    private static PotAllocationMapper potAllocationMapperStatic;
    private static ExpenseAllocationMapper expenseAllocationMapperStatic;
    private static ValueObjectMapper valueObjectMapperStatic;

    private final PotMapper potMapper;
    private final TagMapper tagMapper;
    private final SavedViewMapper savedViewMapper;
    private final TransactionMapper transactionMapper;
    private final PotAllocationMapper potAllocationMapper;
    private final ExpenseAllocationMapper expenseAllocationMapper;
    private final ValueObjectMapper valueObjectMapper;

    public DomainMapper(PotMapper potMapper, TagMapper tagMapper, SavedViewMapper savedViewMapper,
                        TransactionMapper transactionMapper, PotAllocationMapper potAllocationMapper,
                        ExpenseAllocationMapper expenseAllocationMapper, ValueObjectMapper valueObjectMapper) {
        this.potMapper = potMapper;
        this.tagMapper = tagMapper;
        this.savedViewMapper = savedViewMapper;
        this.transactionMapper = transactionMapper;
        this.potAllocationMapper = potAllocationMapper;
        this.expenseAllocationMapper = expenseAllocationMapper;
        this.valueObjectMapper = valueObjectMapper;
    }

    @PostConstruct
    public void init() {
        potMapperStatic = potMapper;
        tagMapperStatic = tagMapper;
        savedViewMapperStatic = savedViewMapper;
        transactionMapperStatic = transactionMapper;
        potAllocationMapperStatic = potAllocationMapper;
        expenseAllocationMapperStatic = expenseAllocationMapper;
        valueObjectMapperStatic = valueObjectMapper;
    }

    public static OffsetDateTime toOdt(Datetime dt) {
        return valueObjectMapperStatic.toOdt(dt);
    }

    public static Datetime toDt(OffsetDateTime odt) {
        return valueObjectMapperStatic.toDt(odt);
    }

    public static PotEntity toJpa(Pot domain) {
        return potMapperStatic.toJpa(domain.data());
    }

    public static Pot toDomain(PotEntity e) {
        return potMapperStatic.toDomain(e);
    }

    public static TagEntity toJpa(Tag domain) {
        return tagMapperStatic.toJpa(domain.data());
    }

    public static Tag toDomain(TagEntity e) {
        return tagMapperStatic.toDomain(e);
    }

    public static TransactionEntity toJpa(IncomeData d) {
        return transactionMapperStatic.toJpa(d);
    }

    public static TransactionEntity toJpa(ExpenseData d) {
        return transactionMapperStatic.toJpa(d);
    }

    public static TransactionEntity toJpa(TransferData d) {
        return transactionMapperStatic.toJpa(d);
    }

    public static TransactionEntity toJpa(com.envaultko.backend.domain.income.CancellationData d) {
        return transactionMapperStatic.toJpa(d);
    }

    public static TransactionEntity toJpa(com.envaultko.backend.domain.expense.CancellationData d) {
        return transactionMapperStatic.toJpa(d);
    }

    public static PotAllocationEntity toJpa(PotAllocation domain) {
        return potAllocationMapperStatic.toJpa(domain.data());
    }

    public static PotAllocation toDomain(PotAllocationEntity e, Id incomeId) {
        return potAllocationMapperStatic.toDomain(e, incomeId);
    }

    public static ExpenseAllocationEntity toJpa(ExpenseAllocation domain) {
        return expenseAllocationMapperStatic.toJpa(domain.data());
    }

    public static ExpenseAllocation toDomain(ExpenseAllocationEntity e, Id expenseId) {
        return expenseAllocationMapperStatic.toDomain(e, expenseId);
    }

    public static PotAllocationEntity potAllocation(Id transactionId, Id potId, Money amount, Datetime at) {
        return transactionMapperStatic.potAllocation(transactionId, potId, amount, at);
    }

    public static ExpenseAllocationEntity expenseAllocation(Id transactionId, Id potId, Money amount, Datetime at) {
        return transactionMapperStatic.expenseAllocation(transactionId, potId, amount, at);
    }

    public static TransactionTagEntity toJpa(String transactionId, String tagId) {
        return transactionMapperStatic.toJpa(transactionId, tagId);
    }

    public static SavedViewEntity toJpa(SavedView domain) {
        return savedViewMapperStatic.toJpa(domain.data());
    }

    public static SavedView toDomain(SavedViewEntity e) {
        return savedViewMapperStatic.toDomain(e);
    }
}
