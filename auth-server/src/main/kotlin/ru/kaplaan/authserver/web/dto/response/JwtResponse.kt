package ru.kaplaan.authserver.web.dto.response


data class JwtResponse(
    val accessToken: String,
    val refreshToken: String,
)


