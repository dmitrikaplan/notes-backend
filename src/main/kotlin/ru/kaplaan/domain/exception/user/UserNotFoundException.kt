package ru.kaplaan.domain.exception.user

class UserNotFoundException(message: String?) : Exception(message){
    override val message: String
        get() = "Пользователь не найден"
}
