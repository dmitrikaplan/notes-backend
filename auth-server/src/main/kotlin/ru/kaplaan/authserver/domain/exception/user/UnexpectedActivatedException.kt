package ru.kaplaan.authserver.domain.exception.user

import ru.kaplaan.domain.domain.exception.UserException

class UnexpectedActivatedException: UserException("Неожидаемое состояние активации аккаунта") {
    override val message: String
        get() = super.message
}