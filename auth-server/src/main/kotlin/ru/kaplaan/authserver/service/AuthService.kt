package ru.kaplaan.authserver.service

import org.springframework.stereotype.Service
import ru.kaplaan.authserver.domain.user.UserIdentification
import ru.kaplaan.authserver.web.dto.response.jwt.JwtResponse
import ru.kaplaan.domain.domain.user.User

@Service
interface AuthService {

    fun registerUser(user: User)

    fun registerAdmin(user: User)

    fun authenticate(userIdentification: UserIdentification): JwtResponse

    fun activateAccount(code: String)

    fun passwordRecovery(userIdentification: UserIdentification)

    fun getNewRefreshToken(jwtToken: String): JwtResponse

}
