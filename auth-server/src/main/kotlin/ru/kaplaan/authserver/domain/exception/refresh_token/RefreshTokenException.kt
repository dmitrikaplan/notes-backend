package ru.kaplaan.authserver.domain.exception.refresh_token

abstract class RefreshTokenException(override val message: String)
    : RuntimeException(message)