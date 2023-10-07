package ru.kaplaan.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.kaplaan.domain.entity.User;
import ru.kaplaan.web.dto.user.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activationCode", ignore = true)
    @Mapping(target = "activated", ignore = true)
    User toEntity(UserDto userDto);

}
