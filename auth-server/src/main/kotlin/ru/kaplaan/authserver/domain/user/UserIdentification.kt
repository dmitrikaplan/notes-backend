package ru.kaplaan.authserver.domain.user


data class UserIdentification(
    var usernameOrEmail: String,
    var password: String
)