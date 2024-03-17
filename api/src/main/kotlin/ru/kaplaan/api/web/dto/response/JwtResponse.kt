package ru.kaplaan.api.web.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Сущность ответа с jwt токенами")
data class JwtResponse(
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    val accessToken: String,

    @Schema(description = "Токен обновления", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2NjY2IiwibmFtZSI6IkphbmUgRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.IOmij52z8H2QmAAtMvhmTbrAzlWAEGl4mmOvSFMOilc")
    val refreshToken: String,
)


