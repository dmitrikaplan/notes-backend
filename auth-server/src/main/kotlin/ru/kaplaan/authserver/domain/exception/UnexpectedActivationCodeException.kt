package ru.kaplaan.authserver.domain.exception

class UnexpectedActivationCodeException: RuntimeException() {
    override val message: String
        get() = "Неожидаемый код активации"
}