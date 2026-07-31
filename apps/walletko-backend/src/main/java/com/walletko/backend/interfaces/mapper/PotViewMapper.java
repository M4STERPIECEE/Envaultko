package com.walletko.backend.interfaces.mapper;

import com.walletko.backend.domain.pot.PotSnapshot;
import com.walletko.backend.interfaces.dto.PotWithBalanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PotViewMapper {

    @Mapping(target = "id", expression = "java(snapshot.pot().id().value())")
    @Mapping(target = "name", expression = "java(snapshot.pot().name().value())")
    @Mapping(target = "percentage", expression = "java(snapshot.pot().percentage().value())")
    @Mapping(target = "color", expression = "java(snapshot.pot().color().value())")
    @Mapping(target = "isDefault", expression = "java(snapshot.pot().isDefault())")
    @Mapping(target = "createdAt", expression = "java(snapshot.pot().createdAt().toOffsetDateTime())")
    @Mapping(target = "balance", expression = "java(snapshot.balance().rawCents())")
    PotWithBalanceDTO toDto(PotSnapshot snapshot);

    List<PotWithBalanceDTO> toDtos(List<PotSnapshot> snapshots);
}
