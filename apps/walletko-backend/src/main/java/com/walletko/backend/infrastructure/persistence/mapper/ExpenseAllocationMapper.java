package com.walletko.backend.infrastructure.persistence.mapper;

import com.walletko.backend.domain.expense.AllocationData;
import com.walletko.backend.domain.expense.ExpenseAllocation;
import com.walletko.backend.domain.shared.vo.Id;
import com.walletko.backend.infrastructure.persistence.entity.ExpenseAllocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ValueObjectMapper.class)
public interface ExpenseAllocationMapper {

    @Mapping(target = "id", expression = "java(domain.id().value())")
    @Mapping(target = "transactionId", expression = "java(domain.expenseId().value())")
    @Mapping(target = "potId", expression = "java(domain.potId().value())")
    @Mapping(target = "amount", expression = "java(domain.amount().rawCents())")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ExpenseAllocationEntity toJpa(AllocationData domain);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "potId", source = "entity.potId")
    @Mapping(target = "expenseId", source = "expenseId")
    @Mapping(target = "amount", source = "entity.amount")
    @Mapping(target = "createdAt", source = "entity.createdAt")
    @Mapping(target = "updatedAt", source = "entity.updatedAt")
    ExpenseAllocation toDomain(ExpenseAllocationEntity entity, Id expenseId);
}
