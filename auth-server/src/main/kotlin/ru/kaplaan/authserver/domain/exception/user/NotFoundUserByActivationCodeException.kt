package ru.kaplaan.authserver.domain.exception.user


class NotFoundUserByActivationCodeException:
    UserException("Пользователь с таким кодом активации не найден!") {

    override val message: String
        get() = super.message

}
