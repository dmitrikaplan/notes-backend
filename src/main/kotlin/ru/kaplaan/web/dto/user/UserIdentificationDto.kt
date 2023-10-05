package ru.kaplaan.web.dto.user

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import org.springframework.validation.annotation.Validated
import ru.kaplaan.web.validation.OnCreate

data class UserIdentificationDto(
    @NotNull(message = "Login or Email must be not null", groups = [OnCreate::class])
    @Length(
        min = 6, max = 320,
        message = "The password must be greater than 7, but less than 321",
        groups = [OnCreate::class]
    )
    private val loginOrEmail: String,

    @Length(
        min = 8, max = 1024,
        message = "The password must be greater than 9, but less than 1025",
        groups = [OnCreate::class]
    )
    private val password: String
) {
    fun getLoginOrEmail() =
        loginOrEmail

    fun getPassword() =
        password
}