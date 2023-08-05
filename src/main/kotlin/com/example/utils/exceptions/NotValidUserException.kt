package com.example.utils.exceptions

class NotValidUserException : Exception(){
    override val message: String
        get() = "Некорректные(й) логин и/или пароль, и/или почта"
}
