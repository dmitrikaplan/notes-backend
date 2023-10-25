package ru.kaplaan.domain.exception.user

class RoleNotFoundException : RuntimeException() {
    override val message: String
        get() = "Данная роль не найдена!"
}
