package ru.kaplaan.domain.exception

class NotValidEmailException : Exception(){
    override val message: String
        get() = "Неверный формат почты"
}
