package ru.kaplaan.dto.mapper;

import org.mapstruct.Mapper;
import ru.kaplaan.domain.user.UserIdentification;
import ru.kaplaan.web.dto.user.UserIdentificationDto;

@Mapper(componentModel = "spring")
public interface UserIdentificationMapper {

    UserIdentificationDto toDto(UserIdentification userIdentification);

    UserIdentification toEntity(UserIdentificationDto userIdentificationDto);

}
