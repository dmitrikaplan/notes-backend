package ru.kaplaan.web.mapper

import ru.kaplaan.domain.entity.user.Role
import ru.kaplaan.domain.entity.user.User
import ru.kaplaan.domain.exception.user.RoleNotFoundException
import ru.kaplaan.domain.user.UserIdentification
import ru.kaplaan.web.dto.user.UserDto
import ru.kaplaan.web.dto.user.UserIdentificationDto
import java.lang.IllegalArgumentException


fun User.toDto(): UserDto {
    return UserDto(
        email = email,
        username = username,
        password = password,
        role = role.name
    )
}


fun UserDto.toEntity(): User {
    try {
        val role = Role.valueOf(role)
        return User(
            email = email,
            username = username,
            password = password,
            role = role
        )
    } catch (e: IllegalArgumentException) {
        throw RoleNotFoundException()
    }
}


fun UserIdentification.toDto(): UserIdentificationDto{
    return UserIdentificationDto(
        usernameOrEmail = usernameOrEmail,
        password = password
    )
}


fun UserIdentificationDto.toEntity(): UserIdentification{
    return UserIdentification(
        usernameOrEmail = usernameOrEmail,
        password = password
    )
}