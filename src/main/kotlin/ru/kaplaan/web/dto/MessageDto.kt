package ru.kaplaan.web.dto

class MessageDto(
    private var message: String?
) {

    fun getMessage() =
        message

    fun setMessage(message: String){
        this.message = message
    }
}