package com.example.utils.exceptions

class UserNotFoundException(message: String?) : Exception(message){
    override val message: String
        get() = "Пользователь не найден"
}
