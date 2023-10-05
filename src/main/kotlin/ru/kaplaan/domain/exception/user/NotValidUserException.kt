package ru.kaplaan.domain.exception.user

class NotValidUserException : Exception(){
    override val message: String
        get() = "Некорректные(й) логин и/или пароль, и/или почта"
}
