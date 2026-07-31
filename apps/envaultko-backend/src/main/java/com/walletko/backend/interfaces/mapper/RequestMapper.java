package com.walletko.backend.interfaces.mapper;

import com.walletko.backend.application.tag.ResolveOwnedTags;
import com.walletko.backend.domain.expense.DrawFrom;
import com.walletko.backend.domain.shared.vo.Datetime;
import com.walletko.backend.domain.shared.vo.Id;
import com.walletko.backend.domain.shared.vo.Money;
import com.walletko.backend.interfaces.dto.DrawFromDTO;
import com.walletko.backend.interfaces.dto.TagInputDTO;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    default List<ResolveOwnedTags.TagInput> toTagInputs(List<TagInputDTO> tags) {
        if (tags == null) return List.of();
        return tags.stream().map(t -> new ResolveOwnedTags.TagInput(t.id(), t.name())).toList();
    }

    default List<DrawFrom> toDrawFroms(List<DrawFromDTO> drawFrom) {
        if (drawFrom == null) return List.of();
        return drawFrom.stream()
            .map(d -> new DrawFrom(new Id(d.potId()), Money.fromCents(d.amount())))
            .toList();
    }

    default Datetime toDatetime(OffsetDateTime odt) {
        return odt != null ? Datetime.of(odt) : null;
    }
}
