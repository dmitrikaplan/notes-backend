package ru.kaplaan.authserver.domain.exception.user



class UserAlreadyRegisteredException(message: String): UserException(message){

    override val message: String
        get() = super.message
}
