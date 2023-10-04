package ru.kaplaan.domain.exception

class UnexpectedActivationCodeException: Exception() {
    override val message: String
        get() = "Неожидаемый код активации"
}