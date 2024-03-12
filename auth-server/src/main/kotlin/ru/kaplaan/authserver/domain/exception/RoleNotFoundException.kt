package ru.kaplaan.authserver.domain.exception

import ru.kaplaan.domain.domain.exception.UserException

class RoleNotFoundException : UserException("Роль не найдена!") {
    override val message: String
        get() = super.message

}
