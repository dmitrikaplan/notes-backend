package ru.kaplaan.authserver.web.mapper

import ru.kaplaan.authserver.domain.entity.Role
import ru.kaplaan.authserver.domain.entity.User
import ru.kaplaan.authserver.domain.exception.user.RoleNotFoundException
import ru.kaplaan.authserver.domain.user.UserIdentification
import ru.kaplaan.authserver.web.dto.user.UserDetailsDto
import ru.kaplaan.authserver.web.dto.user.UserDto
import ru.kaplaan.authserver.web.dto.user.UserIdentificationDto


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


fun UserIdentification.toDto(): UserIdentificationDto {
    return UserIdentificationDto(
        usernameOrEmail = usernameOrEmail,
        password = password
    )
}


fun UserIdentificationDto.toEntity(): UserIdentification {
    return UserIdentification(
        usernameOrEmail = usernameOrEmail,
        password = password
    )
}


fun User.toUserDetailsDto(): UserDetailsDto =
    UserDetailsDto(
        id = this.id,
        email = this.email,
        username = this.username,
        password = this.password,
        activated = this.activated,
        activationCode = this.activationCode,
        role = this.role.name
    )