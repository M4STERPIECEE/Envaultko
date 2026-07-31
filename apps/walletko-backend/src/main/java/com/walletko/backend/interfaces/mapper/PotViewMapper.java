package com.walletko.backend.interfaces.mapper;

import com.walletko.backend.domain.pot.PotSnapshot;
import com.walletko.backend.domain.pot.PotUpdate;
import com.walletko.backend.domain.shared.vo.Id;
import com.walletko.backend.domain.shared.vo.Money;
import com.walletko.backend.interfaces.dto.OtherPotDTO;
import com.walletko.backend.interfaces.dto.PotAllocationDTO;
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

    default List<PotUpdate> toPotUpdatesFromOtherPots(List<OtherPotDTO> otherPots) {
        return otherPots.stream()
            .map(o -> new PotUpdate(new Id(o.id()), o.percentage()))
            .toList();
    }

    default List<PotUpdate> toPotUpdatesFromAllocations(List<PotAllocationDTO> allocations) {
        return allocations.stream()
            .map(p -> new PotUpdate(new Id(p.id()), p.percentage()))
            .toList();
    }

    default Id toId(String id) {
        return id != null ? new Id(id) : null;
    }

    default Money toMoney(long cents) {
        return Money.fromCents(cents);
    }
}
