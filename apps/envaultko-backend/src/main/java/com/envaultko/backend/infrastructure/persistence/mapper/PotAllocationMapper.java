package com.envaultko.backend.infrastructure.persistence.mapper;

import com.envaultko.backend.domain.income.AllocationData;
import com.envaultko.backend.domain.income.PotAllocation;
import com.envaultko.backend.domain.shared.vo.Id;
import com.envaultko.backend.infrastructure.persistence.entity.PotAllocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ValueObjectMapper.class)
public interface PotAllocationMapper {

    @Mapping(target = "id", expression = "java(domain.id().value())")
    @Mapping(target = "transactionId", expression = "java(domain.incomeId().value())")
    @Mapping(target = "potId", expression = "java(domain.potId().value())")
    @Mapping(target = "amount", expression = "java(domain.amount().rawCents())")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    PotAllocationEntity toJpa(AllocationData domain);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "potId", source = "entity.potId")
    @Mapping(target = "incomeId", source = "incomeId")
    @Mapping(target = "amount", source = "entity.amount")
    @Mapping(target = "createdAt", source = "entity.createdAt")
    @Mapping(target = "updatedAt", source = "entity.updatedAt")
    PotAllocation toDomain(PotAllocationEntity entity, Id incomeId);
}
