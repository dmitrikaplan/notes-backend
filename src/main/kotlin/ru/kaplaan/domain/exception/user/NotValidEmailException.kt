package ru.kaplaan.domain.exception.user

class NotValidEmailException : Exception(){
    override val message: String
        get() = "Неверный формат почты"
}
