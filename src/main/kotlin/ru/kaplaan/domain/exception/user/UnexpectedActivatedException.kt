package ru.kaplaan.domain.exception.user

class UnexpectedActivatedException: Exception() {
    override val message: String
        get() = "Неожидаемое состояние активации аккаунта"
}