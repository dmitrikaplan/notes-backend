package ru.kaplaan.service

import org.springframework.stereotype.Service
import ru.kaplaan.domain.entity.User
import ru.kaplaan.domain.user.UserIdentification
import ru.kaplaan.web.dto.response.jwt.JwtResponse

@Service
interface AuthService {

    fun registration(user: User)

    fun login(userIdentification: UserIdentification): JwtResponse

    fun activateAccount(code: String)

    fun passwordRecovery(loginOrEmail: String)

}
