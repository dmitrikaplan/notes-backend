package com.example.utils.exceptions

class UnexpectedActivatedException: Exception() {
    override val message: String
        get() = "Неожидаемое состояние активации аккаунта"
}