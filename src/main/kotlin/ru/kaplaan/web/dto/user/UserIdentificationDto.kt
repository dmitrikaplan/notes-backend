package ru.kaplaan.web.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.springframework.validation.annotation.Validated
import ru.kaplaan.web.validation.OnCreate
import ru.kaplaan.web.validation.OnRecovery

@Schema(description = "Сущность пользователя для идентификации")
data class UserIdentificationDto(
    @Schema(description = "username или пароль пользователя", example = "account@yandex.ru")
    @NotNull(message = "Username or Email must be not null", groups = [OnCreate::class, OnRecovery::class])
    @Length(
        min = 6, max = 320,
        message = "The password must be greater than 7, but less than 321",
        groups = [OnCreate::class]
    )
    val usernameOrEmail: String,

    @Schema(description = "пароль пользователя", example = "123456")
    @Length(
        min = 8, max = 1024,
        message = "The password must be greater than 9, but less than 1025",
        groups = [OnCreate::class]
    )
    val password: String
)