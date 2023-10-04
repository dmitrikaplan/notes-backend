package ru.kaplaan.domain.exception

class UnexpectedActivatedException: Exception() {
    override val message: String
        get() = "Неожидаемое состояние активации аккаунта"
}