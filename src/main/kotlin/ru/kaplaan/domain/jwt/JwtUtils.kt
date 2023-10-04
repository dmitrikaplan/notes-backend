package ru.kaplaan.domain.jwt

import io.jsonwebtoken.Claims
import ru.kaplaan.domain.security.JwtAuthentication

object JwtUtils {
    fun generate(claims: Claims): JwtAuthentication {
        val jwtInfoToken = JwtAuthentication()
        jwtInfoToken.setLogin(claims.subject)
        return jwtInfoToken
    }
}
