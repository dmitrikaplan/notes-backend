package ru.kaplaan.authserver.domain.exception

class UnexpectedActivatedException: RuntimeException() {
    override val message: String
        get() = "Неожидаемое состояние активации аккаунта"
}