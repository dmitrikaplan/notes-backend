package ru.kaplaan.authserver.domain.exception.user


class UnexpectedActivationCodeException: UserException("Неожидаемый код активации") {
    override val message: String
        get() = super.message

}