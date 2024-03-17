package ru.kaplaan.authserver.web.dto.user

import jakarta.persistence.*
import ru.kaplaan.authserver.domain.entity.Role

data class UserDetailsDto(
    var id: Long?,
    val email: String,
    val username: String,
    val password: String,
    val activated: Boolean,
    val activationCode: String?,
    val role: String
)