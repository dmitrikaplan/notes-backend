package ru.kaplaan.authserver.web.dto.user

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import ru.kaplaan.authserver.web.validation.OnCreate
import ru.kaplaan.authserver.web.validation.OnRecovery

data class UserIdentificationDto(
    @field:NotNull(message = "Username or Email must be not null", groups = [OnCreate::class, OnRecovery::class])
    @field:Length(
        min = 6, max = 320,
        message = "The password must be greater than 7, but less than 321",
        groups = [OnCreate::class]
    )
    val usernameOrEmail: String,

    @field:Length(
        min = 8, max = 1024,
        message = "The password must be greater than 9, but less than 1025",
        groups = [OnCreate::class]
    )
    val password: String
)