package ru.kaplaan.authserver.domain.exception.user

abstract class UserException(override val message: String):
    RuntimeException(message)