package com.envaultko.backend.interfaces.mapper;

import com.envaultko.backend.domain.auth.AuthenticatedUser;
import com.envaultko.backend.interfaces.dto.AuthSessionDTO;
import com.envaultko.backend.interfaces.dto.AuthUserDTO;
import com.envaultko.backend.interfaces.dto.SessionRefDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthViewMapper {

    @Mapping(target = "id", expression = "java(user.id().value())")
    AuthUserDTO toUser(AuthenticatedUser user);

    @Mapping(target = "user", expression = "java(toUser(user))")
    @Mapping(target = "session", expression = "java(new SessionRefDTO(user.id().value()))")
    AuthSessionDTO toSession(AuthenticatedUser user);
}
