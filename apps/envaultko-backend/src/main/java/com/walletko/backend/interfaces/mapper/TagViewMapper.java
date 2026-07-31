package com.walletko.backend.interfaces.mapper;

import com.walletko.backend.domain.tag.Tag;
import com.walletko.backend.interfaces.dto.TagRefDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagViewMapper {

    @Mapping(target = "id", expression = "java(tag.id().value())")
    @Mapping(target = "name", expression = "java(tag.name().value())")
    TagRefDTO toDto(Tag tag);

    List<TagRefDTO> toDtos(List<Tag> tags);
}
