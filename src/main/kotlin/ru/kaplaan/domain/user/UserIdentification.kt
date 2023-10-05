package ru.kaplaan.domain.user

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import ru.kaplaan.web.validation.OnCreate

data class UserIdentification(
    private val loginOrEmail: String,
    private val password: String
) {
    fun getLoginOrEmail() =
        loginOrEmail

    fun getPassword() =
        password
}