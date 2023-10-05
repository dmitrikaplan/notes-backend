package ru.kaplaan.domain.exception.user

class UnexpectedActivationCodeException: Exception() {
    override val message: String
        get() = "Неожидаемый код активации"
}