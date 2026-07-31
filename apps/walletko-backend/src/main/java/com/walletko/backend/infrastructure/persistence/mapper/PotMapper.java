package com.walletko.backend.infrastructure.persistence.mapper;

import com.walletko.backend.domain.pot.Pot;
import com.walletko.backend.domain.pot.PotData;
import com.walletko.backend.infrastructure.persistence.entity.PotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ValueObjectMapper.class)
public interface PotMapper {

    @Mapping(target = "id", expression = "java(domain.id().value())")
    @Mapping(target = "name", expression = "java(domain.name().value())")
    @Mapping(target = "percentage", expression = "java(domain.percentage().value())")
    @Mapping(target = "color", expression = "java(domain.color().value())")
    @Mapping(target = "default", source = "isDefault")
    @Mapping(target = "userId", expression = "java(domain.userId().value())")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "archivedAt", source = "archivedAt")
    PotEntity toJpa(PotData domain);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "percentage", source = "percentage")
    @Mapping(target = "color", source = "color")
    @Mapping(target = "isDefault", source = "default")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "archivedAt", source = "archivedAt")
    Pot toDomain(PotEntity entity);
}
