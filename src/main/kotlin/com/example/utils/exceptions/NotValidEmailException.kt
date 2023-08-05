package com.example.utils.exceptions

class NotValidEmailException : Exception(){
    override val message: String
        get() = "Неверный формат почты"
}
