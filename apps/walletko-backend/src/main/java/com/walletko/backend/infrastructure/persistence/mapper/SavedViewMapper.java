package com.walletko.backend.infrastructure.persistence.mapper;

import com.walletko.backend.domain.savedview.SavedView;
import com.walletko.backend.domain.savedview.ViewData;
import com.walletko.backend.domain.shared.vo.Id;
import com.walletko.backend.infrastructure.persistence.entity.SavedViewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ValueObjectMapper.class)
public interface SavedViewMapper {

    @Mapping(target = "id", expression = "java(domain.id().value())")
    @Mapping(target = "userId", expression = "java(domain.userId().value())")
    @Mapping(target = "name", expression = "java(domain.name().value())")
    @Mapping(target = "tagIds", source = "tagIds", qualifiedByName = "toPgArray")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    SavedViewEntity toJpa(ViewData domain);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "tagIds", source = "tagIds", qualifiedByName = "fromPgArray")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    SavedView toDomain(SavedViewEntity entity);

    @Named("toPgArray")
    default String toPgArray(List<Id> ids) {
        if (ids == null || ids.isEmpty()) return "{}";
        return ids.stream().map(id -> "\"" + id.value() + "\"")
                  .collect(Collectors.joining(",", "{", "}"));
    }

    @Named("fromPgArray")
    default List<Id> fromPgArray(String pgArray) {
        if (pgArray == null || pgArray.equals("{}") || pgArray.isBlank()) return List.of();
        String trimmed = pgArray.substring(1, pgArray.length() - 1);
        return Arrays.stream(trimmed.split(","))
            .map(s -> s.replace("\"", "").trim())
            .filter(s -> !s.isEmpty())
            .map(Id::new)
            .toList();
    }
}
