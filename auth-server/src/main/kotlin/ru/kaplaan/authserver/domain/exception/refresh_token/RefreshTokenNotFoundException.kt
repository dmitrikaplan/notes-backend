package ru.kaplaan.authserver.domain.exception.refresh_token

class RefreshTokenNotFoundException: RefreshTokenException("Refresh token не найден!") {

    override val message: String
        get() = super.message
}