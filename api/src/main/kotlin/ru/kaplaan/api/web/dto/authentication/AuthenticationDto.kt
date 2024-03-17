package ru.kaplaan.api.web.dto.authentication


data class AuthenticationDto(
    val principal: Any,
    val credentials: Any,
    val authorities: List<String>,
)