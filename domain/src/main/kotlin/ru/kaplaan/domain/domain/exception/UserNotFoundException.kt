package ru.kaplaan.domain.domain.exception

class UserNotFoundException(message: String?)
    : UserException(message ?: "Пользователь не найден"){

    override val message: String =
        super.message
}
