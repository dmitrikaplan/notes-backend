package ru.kaplaan.api.web.mapper

import ru.kaplaan.api.domain.user.Role
import ru.kaplaan.api.domain.user.User
import ru.kaplaan.api.web.dto.user.UserDto
import javax.management.relation.RoleNotFoundException


fun User.toDto(): UserDto {
    return UserDto(
        email = email,
        username = username,
        password = password
    )
}


fun UserDto.toEntity(role: Role): User {
    try {
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
