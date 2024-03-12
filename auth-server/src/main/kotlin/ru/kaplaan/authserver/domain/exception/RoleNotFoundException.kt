package ru.kaplaan.authserver.domain.exception

class RoleNotFoundException : RuntimeException() {
    override val message: String
        get() = "Данная роль не найдена!"
}
