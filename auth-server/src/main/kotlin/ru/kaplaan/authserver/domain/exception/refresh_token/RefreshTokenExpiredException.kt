package ru.kaplaan.authserver.domain.exception.refresh_token

import ru.kaplaan.domain.domain.exception.UserException

class RefreshTokenExpiredException: RefreshTokenException("Срок действия refresh токена истек!") {

    override val message: String
        get() = super.message
}