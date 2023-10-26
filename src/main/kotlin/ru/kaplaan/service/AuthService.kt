package ru.kaplaan.service

import org.springframework.stereotype.Service
import ru.kaplaan.domain.entity.user.User
import ru.kaplaan.domain.user.UserIdentification
import ru.kaplaan.web.dto.response.jwt.JwtResponse

@Service
interface AuthService {

    fun register(user: User)

    fun authenticate(userIdentification: UserIdentification): JwtResponse

    fun activateAccount(code: String)

    fun passwordRecovery(userIdentification: UserIdentification)

    fun getNewRefreshToken(jwtToken: String): JwtResponse

}
