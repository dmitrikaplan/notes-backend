package ru.kaplaan.authserver.domain.exception

class NotValidUserException : RuntimeException(){
    override val message: String
        get() = "Некорректные(й) логин и/или пароль, и/или почта"
}
