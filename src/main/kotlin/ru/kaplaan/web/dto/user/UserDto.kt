package ru.kaplaan.web.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import ru.kaplaan.web.validation.OnCreate

@Schema(description = "Сущность пользователя")
data class UserDto(
    @Schema(description = "Почта пользователя", example = "account@yandex.ru")
    @Email(message = "Email should fit the email pattern ", groups = [OnCreate::class])
    val email: String,

    @Schema(description = "Логин пользователя", example = "username")
    @Pattern(
        regexp = "^[a-zA-Z0-9]{6,320}$",
        message = "Login should fit the login pattern",
        groups = [OnCreate::class]
    )
    val login: String,

    @Schema(description = "Пароль пользователя", example = "123456")
    @Length(
        min = 8, max = 1024,
        message = "The password must be greater than 9, but less than 1025",
        groups = [OnCreate::class]
    )
    val password: String,

)