package ru.kaplaan.domain.domain.exception



class UserAlreadyRegisteredException(message: String): UserException(message){

    override val message: String
        get() = super.message
}
