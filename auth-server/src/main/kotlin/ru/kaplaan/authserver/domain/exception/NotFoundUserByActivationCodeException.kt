package ru.kaplaan.authserver.domain.exception

import ru.kaplaan.domain.domain.exception.UserException

class NotFoundUserByActivationCodeException:
    UserException("Пользователь с таким кодом активации не найден!") {

    override val message: String
        get() = super.message


}
