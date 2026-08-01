package com.envaultko.backend.infrastructure.persistence.mapper;

import com.envaultko.backend.domain.savedview.SavedView;
import com.envaultko.backend.domain.savedview.ViewData;
import com.envaultko.backend.domain.shared.vo.Id;
import com.envaultko.backend.infrastructure.persistence.entity.SavedViewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = ValueObjectMapper.class)
public interface SavedViewMapper {

    @Mapping(target = "id", expression = "java(domain.id().value())")
    @Mapping(target = "userId", expression = "java(domain.userId().value())")
    @Mapping(target = "name", expression = "java(domain.name().value())")
    @Mapping(target = "tagIds", source = "tagIds")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    SavedViewEntity toJpa(ViewData domain);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "tagIds", source = "tagIds")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    SavedView toDomain(SavedViewEntity entity);

    default List<Id> toIds(List<String> ids) {
        return ids != null ? ids.stream().map(Id::new).toList() : List.of();
    }

    default List<String> toStrings(List<Id> ids) {
        return ids != null ? ids.stream().map(Id::value).toList() : List.of();
    }
}
