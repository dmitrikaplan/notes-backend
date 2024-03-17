package ru.kaplaan.authserver.domain.exception.user


class UnexpectedActivatedException: UserException("Неожидаемое состояние активации аккаунта") {
    override val message: String
        get() = super.message
}