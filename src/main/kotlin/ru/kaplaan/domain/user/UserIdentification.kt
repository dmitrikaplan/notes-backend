package ru.kaplaan.domain.user


data class UserIdentification(
    var loginOrEmail: String,
    var password: String
)