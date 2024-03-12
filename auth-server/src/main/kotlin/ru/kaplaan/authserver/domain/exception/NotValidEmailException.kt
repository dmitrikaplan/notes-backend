package ru.kaplaan.authserver.domain.exception

class NotValidEmailException : RuntimeException(){
    override val message: String
        get() = "Неверный формат почты"
}
