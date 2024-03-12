package ru.kaplaan.authserver.domain.exception

import ru.kaplaan.domain.domain.exception.UserException

class UnexpectedActivationCodeException: UserException("Неожидаемый код активации") {
    override val message: String
        get() = super.message
}