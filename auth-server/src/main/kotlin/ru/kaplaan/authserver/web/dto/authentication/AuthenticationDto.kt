package ru.kaplaan.authserver.web.dto.authentication

import org.springframework.security.core.GrantedAuthority

data class AuthenticationDto(
    val principal: Any,
    val credentials: Any,
    val authorities: List<String>,
)