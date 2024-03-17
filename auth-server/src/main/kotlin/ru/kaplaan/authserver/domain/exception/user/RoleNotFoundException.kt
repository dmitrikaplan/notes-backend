package ru.kaplaan.authserver.domain.exception.user

class RoleNotFoundException : UserException("Роль не найдена!") {
    override val message: String
        get() = super.message

}
