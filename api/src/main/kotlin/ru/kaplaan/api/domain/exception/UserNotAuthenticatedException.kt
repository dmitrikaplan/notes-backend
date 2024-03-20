package ru.kaplaan.api.domain.exception

import org.springframework.security.core.AuthenticationException

class UserNotAuthenticatedException: AuthenticationException("Пользователь не аутентифицирован!") {

    override val message: String
        get() = super.message!!
}