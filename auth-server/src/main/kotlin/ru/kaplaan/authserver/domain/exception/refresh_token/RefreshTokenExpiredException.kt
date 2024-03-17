package ru.kaplaan.authserver.domain.exception.refresh_token

class RefreshTokenExpiredException: RefreshTokenException("Срок действия refresh токена истек!") {

    override val message: String
        get() = super.message
}