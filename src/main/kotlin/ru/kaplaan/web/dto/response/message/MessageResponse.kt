package ru.kaplaan.web.dto.response.message

class MessageResponse(
    private var message: String?
) {

    fun getMessage() =
        message

    fun setMessage(message: String){
        this.message = message
    }
}