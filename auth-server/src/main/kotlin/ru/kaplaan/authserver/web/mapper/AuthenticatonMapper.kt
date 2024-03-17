package ru.kaplaan.authserver.web.mapper

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import ru.kaplaan.authserver.web.dto.authentication.AuthenticationDto


fun Authentication.toDto() =
    AuthenticationDto(
        this.name,
        this.credentials as String
    )



fun AuthenticationDto.toUsernamePasswordAuthentication() =
    UsernamePasswordAuthenticationToken
        .unauthenticated(this.username, this.password)!!