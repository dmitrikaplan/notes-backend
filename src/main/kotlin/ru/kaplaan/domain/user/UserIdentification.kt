package ru.kaplaan.domain.user


data class UserIdentification(
    var usernameOrEmail: String,
    var password: String
)