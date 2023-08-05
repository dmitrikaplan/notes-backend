package com.example.utils.exceptions

class UnexpectedActivationCodeException: Exception() {
    override val message: String
        get() = "Неожидаемый код активации"
}