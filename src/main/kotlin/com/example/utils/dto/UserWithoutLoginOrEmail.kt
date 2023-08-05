package com.example.utils.dto

import org.hibernate.validator.constraints.Length
import org.springframework.validation.annotation.Validated

@Validated
data class UserWithoutLoginOrEmail(
    @Length(min = 6, max = 320)
    private val loginOrEmail: String,
    @Length(min = 8, max = 1024)
    private val password: String
) {
    fun getLoginOrEmail() =
        loginOrEmail

    fun getPassword() =
        password
}