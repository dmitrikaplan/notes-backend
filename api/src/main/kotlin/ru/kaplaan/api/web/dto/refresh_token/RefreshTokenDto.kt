package ru.kaplaan.api.web.dto.refresh_token

import jakarta.validation.constraints.NotBlank

data class RefreshTokenDto(
    @NotBlank(message = "Refresh token не должен быть пустым!")
    val refreshToken: String
)