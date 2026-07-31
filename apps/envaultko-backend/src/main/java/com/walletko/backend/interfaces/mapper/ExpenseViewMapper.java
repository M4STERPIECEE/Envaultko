package com.walletko.backend.interfaces.mapper;

import com.walletko.backend.domain.expense.Expense;
import com.walletko.backend.domain.expense.ExpenseAllocation;
import com.walletko.backend.domain.expense.ExpenseData;
import com.walletko.backend.interfaces.dto.AllocationDTO;
import com.walletko.backend.interfaces.dto.CancelPreviewDTO;
import com.walletko.backend.interfaces.dto.ExpenseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = TagViewMapper.class)
public interface ExpenseViewMapper {

    @Mapping(target = "id", expression = "java(data.id().value())")
    @Mapping(target = "name", expression = "java(data.name().value())")
    @Mapping(target = "amount", expression = "java(data.amount().rawCents())")
    @Mapping(target = "createdAt", expression = "java(data.createdAt().toOffsetDateTime())")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "allocations", source = "allocations")
    ExpenseDTO toDto(ExpenseData data);

    default ExpenseDTO toDto(Expense expense) {
        return toDto(expense.data());
    }

    @Mapping(target = "potId", expression = "java(a.potId().value())")
    @Mapping(target = "amount", expression = "java(a.amount().rawCents())")
    AllocationDTO toAllocation(ExpenseAllocation a);

    default List<AllocationDTO> toAllocations(List<ExpenseAllocation> allocations) {
        return allocations.stream().map(this::toAllocation).toList();
    }

    default CancelPreviewDTO toCancelPreview(List<ExpenseAllocation> allocations) {
        return new CancelPreviewDTO(toAllocations(allocations));
    }
}
