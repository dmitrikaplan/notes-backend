package ru.kaplaan.api.domain.exception

import org.springframework.security.core.AuthenticationException

class JwtTokenNotFoundException: AuthenticationException("Jwt token не найден!") {
    override val message: String
        get() = super.message!!
}