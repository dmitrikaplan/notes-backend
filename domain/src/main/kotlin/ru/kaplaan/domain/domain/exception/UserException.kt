package ru.kaplaan.domain.domain.exception

abstract class UserException(override val message: String):
    RuntimeException(message)