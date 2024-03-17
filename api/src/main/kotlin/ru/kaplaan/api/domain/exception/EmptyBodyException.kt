package ru.kaplaan.api.domain.exception

class EmptyBodyException: RuntimeException() {

    override val message: String
        get() = "Получено пустое тело ответа с сервера!"
}