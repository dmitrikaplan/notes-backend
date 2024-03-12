package ru.kaplaan.domain.domain.exception

class UserNotFoundException(message: String?) : RuntimeException(message){
    override val message: String
        get() = "Пользователь не найден"
}
