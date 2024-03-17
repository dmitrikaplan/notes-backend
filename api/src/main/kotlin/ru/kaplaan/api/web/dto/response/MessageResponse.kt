package ru.kaplaan.api.web.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Сущность текстового ответа")
data class MessageResponse(
    @Schema(description = "Сообщение ответа", example = "Ошибка регистрации!")
    val message: String
)