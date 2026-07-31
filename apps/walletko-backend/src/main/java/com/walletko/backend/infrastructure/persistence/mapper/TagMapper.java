package com.walletko.backend.infrastructure.persistence.mapper;

import com.walletko.backend.domain.tag.Tag;
import com.walletko.backend.domain.tag.TagData;
import com.walletko.backend.infrastructure.persistence.entity.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ValueObjectMapper.class)
public interface TagMapper {

    @Mapping(target = "id", expression = "java(domain.id().value())")
    @Mapping(target = "name", expression = "java(domain.name().value())")
    @Mapping(target = "userId", expression = "java(domain.userId().value())")
    @Mapping(target = "createdAt", source = "createdAt")
    TagEntity toJpa(TagData domain);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", ignore = true)
    Tag toDomain(TagEntity entity);
}
