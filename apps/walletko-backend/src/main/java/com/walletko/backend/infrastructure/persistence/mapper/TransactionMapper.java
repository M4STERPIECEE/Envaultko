package com.walletko.backend.infrastructure.persistence.mapper;

import com.walletko.backend.domain.expense.CancellationData;
import com.walletko.backend.domain.expense.ExpenseData;
import com.walletko.backend.domain.income.IncomeData;
import com.walletko.backend.domain.pot.TransferData;
import com.walletko.backend.domain.shared.vo.Datetime;
import com.walletko.backend.domain.shared.vo.Id;
import com.walletko.backend.domain.shared.vo.Money;
import com.walletko.backend.infrastructure.persistence.entity.ExpenseAllocationEntity;
import com.walletko.backend.infrastructure.persistence.entity.PotAllocationEntity;
import com.walletko.backend.infrastructure.persistence.entity.TransactionEntity;
import com.walletko.backend.infrastructure.persistence.entity.TransactionTagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = ValueObjectMapper.class)
public abstract class TransactionMapper {

    @Autowired
    protected ValueObjectMapper valueObjectMapper;

    @Mapping(target = "type",                  constant = "income")
    @Mapping(target = "id",                    expression = "java(d.id().value())")
    @Mapping(target = "name",                  expression = "java(d.name().value())")
    @Mapping(target = "amount",                expression = "java(d.amount().rawCents())")
    @Mapping(target = "userId",                expression = "java(d.userId().value())")
    @Mapping(target = "cancelsTransactionId",  ignore = true)
    @Mapping(target = "createdAt",             source = "createdAt")
    @Mapping(target = "updatedAt",             source = "updatedAt")
    public abstract TransactionEntity toJpa(IncomeData d);

    @Mapping(target = "type",                  constant = "expense")
    @Mapping(target = "id",                    expression = "java(d.id().value())")
    @Mapping(target = "name",                  expression = "java(d.name().value())")
    @Mapping(target = "amount",                expression = "java(d.amount().rawCents())")
    @Mapping(target = "userId",                expression = "java(d.userId().value())")
    @Mapping(target = "cancelsTransactionId",  ignore = true)
    @Mapping(target = "createdAt",             source = "createdAt")
    @Mapping(target = "updatedAt",             source = "updatedAt")
    public abstract TransactionEntity toJpa(ExpenseData d);

    @Mapping(target = "type",                  constant = "transfer")
    @Mapping(target = "id",                    expression = "java(d.id().value())")
    @Mapping(target = "name",                  expression = "java(d.name().value())")
    @Mapping(target = "amount",                expression = "java(d.amount().rawCents())")
    @Mapping(target = "userId",                expression = "java(d.userId().value())")
    @Mapping(target = "cancelsTransactionId",  ignore = true)
    @Mapping(target = "createdAt",             source = "createdAt")
    @Mapping(target = "updatedAt",             source = "createdAt")
    public abstract TransactionEntity toJpa(TransferData d);

    @Mapping(target = "type",                  constant = "income_cancellation")
    @Mapping(target = "id",                    expression = "java(d.id().value())")
    @Mapping(target = "name",                  expression = "java(d.name().value())")
    @Mapping(target = "amount",                expression = "java(d.amount().rawCents())")
    @Mapping(target = "userId",                expression = "java(d.userId().value())")
    @Mapping(target = "cancelsTransactionId",  expression = "java(d.cancelsTransactionId().value())")
    @Mapping(target = "createdAt",             source = "createdAt")
    @Mapping(target = "updatedAt",             source = "createdAt")
    public abstract TransactionEntity toJpa(com.walletko.backend.domain.income.CancellationData d);

    @Mapping(target = "type",                  constant = "expense_cancellation")
    @Mapping(target = "id",                    expression = "java(d.id().value())")
    @Mapping(target = "name",                  expression = "java(d.name().value())")
    @Mapping(target = "amount",                expression = "java(d.amount().rawCents())")
    @Mapping(target = "userId",                expression = "java(d.userId().value())")
    @Mapping(target = "cancelsTransactionId",  expression = "java(d.cancelsTransactionId().value())")
    @Mapping(target = "createdAt",             source = "createdAt")
    @Mapping(target = "updatedAt",             source = "createdAt")
    public abstract TransactionEntity toJpa(CancellationData d);

    public PotAllocationEntity potAllocation(Id transactionId, Id potId, Money amount, Datetime at) {
        var e = new PotAllocationEntity();
        e.setId(Id.generate().value());
        e.setTransactionId(transactionId.value());
        e.setPotId(potId.value());
        e.setAmount(amount.rawCents());
        e.setCreatedAt(valueObjectMapper.toOdt(at));
        e.setUpdatedAt(valueObjectMapper.toOdt(at));
        return e;
    }

    public ExpenseAllocationEntity expenseAllocation(Id transactionId, Id potId, Money amount, Datetime at) {
        var e = new ExpenseAllocationEntity();
        e.setId(Id.generate().value());
        e.setTransactionId(transactionId.value());
        e.setPotId(potId.value());
        e.setAmount(amount.rawCents());
        e.setCreatedAt(valueObjectMapper.toOdt(at));
        e.setUpdatedAt(valueObjectMapper.toOdt(at));
        return e;
    }

    public TransactionTagEntity toJpa(String transactionId, String tagId) {
        return new TransactionTagEntity(transactionId, tagId);
    }
}
