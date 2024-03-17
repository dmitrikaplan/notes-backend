package ru.kaplaan.api.web.mapper

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import ru.kaplaan.api.domain.user.Role
import ru.kaplaan.api.web.dto.authentication.AuthenticationDto


fun Authentication.toDto() =
    AuthenticationDto(
        principal = this.principal,
        credentials = this.credentials,
        authorities = this.authorities.map { it.authority },
    )



fun AuthenticationDto.toUsernamePasswordAuthentication() =
    UsernamePasswordAuthenticationToken.authenticated(
        this.principal,
        this.credentials,
        this.authorities.map { Role.valueOf(it) }
    )!!