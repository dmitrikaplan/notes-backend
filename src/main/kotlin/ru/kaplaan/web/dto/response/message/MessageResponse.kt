package ru.kaplaan.web.dto.response.message

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Сущность текстового ответа")
class MessageResponse(
    @Schema(description = "Сообщение ответа", example = "Ошибка регистрации!")
    private var message: String?
) {

    fun getMessage() =
        message

    fun setMessage(message: String){
        this.message = message
    }
}