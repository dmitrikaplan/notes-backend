package ru.kaplaan.api.web.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import ru.kaplaan.api.web.validation.OnCreate

@Schema(description = "Сущность пользователя")
class UserDto(

    @Schema(description = "Почта пользователя", example = "account@yandex.ru")
    @field:Email(message = "Email should fit the email pattern ", groups = [OnCreate::class])
    var email: String,

    @Schema(description = "Логин пользователя", example = "username")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9]{6,320}$",
        message = "Login should fit the username pattern",
        groups = [OnCreate::class]
    )
    var username: String,

    @Schema(description = "Пароль пользователя", example = "123456")
    @field:Length(
        min = 8, max = 1024,
        message = "The password must be greater than 9, but less than 1025",
        groups = [OnCreate::class]
    )
    var password: String
)