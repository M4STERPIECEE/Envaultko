package com.envaultko.backend.interfaces.mapper;

import com.envaultko.backend.domain.income.Income;
import com.envaultko.backend.domain.income.IncomeData;
import com.envaultko.backend.domain.income.PotAllocation;
import com.envaultko.backend.interfaces.dto.AllocationDTO;
import com.envaultko.backend.interfaces.dto.CancelPreviewDTO;
import com.envaultko.backend.interfaces.dto.IncomeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = TagViewMapper.class)
public interface IncomeViewMapper {

    @Mapping(target = "id", expression = "java(data.id().value())")
    @Mapping(target = "name", expression = "java(data.name().value())")
    @Mapping(target = "amount", expression = "java(data.amount().rawCents())")
    @Mapping(target = "createdAt", expression = "java(data.createdAt().toOffsetDateTime())")
    @Mapping(target = "tags", source = "tags")
    IncomeDTO toDto(IncomeData data);

    default IncomeDTO toDto(Income income) {
        return toDto(income.data());
    }

    @Mapping(target = "potId", expression = "java(a.potId().value())")
    @Mapping(target = "amount", expression = "java(a.amount().rawCents())")
    AllocationDTO toAllocation(PotAllocation a);

    default CancelPreviewDTO toCancelPreview(List<PotAllocation> allocations) {
        return new CancelPreviewDTO(allocations.stream().map(this::toAllocation).toList());
    }
}
