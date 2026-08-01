package com.envaultko.backend.interfaces.mapper;

import com.envaultko.backend.domain.savedview.SavedView;
import com.envaultko.backend.domain.savedview.ViewData;
import com.envaultko.backend.domain.shared.vo.Id;
import com.envaultko.backend.interfaces.dto.SavedViewListItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SavedViewDtoMapper {

    @Mapping(target = "id", expression = "java(data.id().value())")
    @Mapping(target = "name", expression = "java(data.name().value())")
    @Mapping(target = "tagIds", expression = "java(data.tagIds().stream().map(t -> t.value()).toList())")
    @Mapping(target = "createdAt", expression = "java(data.createdAt().toOffsetDateTime())")
    SavedViewListItemDTO toDto(ViewData data);

    default SavedViewListItemDTO toDto(SavedView view) {
        return toDto(view.data());
    }

    default List<SavedViewListItemDTO> toDtos(List<SavedView> views) {
        return views.stream().map(this::toDto).toList();
    }

    default List<String> tagIdValues(SavedView view) {
        return view.tagIds().stream().map(Id::value).toList();
    }

    default List<Id> toIds(List<String> tagIds) {
        return tagIds != null ? tagIds.stream().map(Id::new).toList() : List.of();
    }
}
