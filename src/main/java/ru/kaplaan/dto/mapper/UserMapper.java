package ru.kaplaan.dto.mapper;

import org.mapstruct.Mapper;
import ru.kaplaan.domain.entity.User;
import ru.kaplaan.web.dto.user.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDto toDto(User user);

    User toEntity(UserDto userDto);

}
