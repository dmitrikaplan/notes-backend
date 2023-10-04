package ru.kaplaan.service

import org.springframework.stereotype.Service
import ru.kaplaan.domain.entity.User
import ru.kaplaan.web.dto.JwtResponse
import ru.kaplaan.web.dto.UserWithoutLoginOrEmail

@Service
interface AuthService {

    fun registration(user: User)

    fun login(userWithoutLoginOrEmail: UserWithoutLoginOrEmail): JwtResponse

    fun activateAccount(code: String)

    fun passwordRecovery(userWithoutLoginOrEmail: UserWithoutLoginOrEmail)
}
