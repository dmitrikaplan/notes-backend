package ru.kaplaan.service.impl

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.kaplaan.domain.entity.RefreshToken
import ru.kaplaan.domain.entity.user.User
import ru.kaplaan.domain.exception.user.*
import ru.kaplaan.service.JwtService
import ru.kaplaan.domain.user.UserIdentification
import ru.kaplaan.repository.RefreshTokenRepository
import ru.kaplaan.repository.UserRepository
import ru.kaplaan.service.AuthService
import ru.kaplaan.service.EmailService
import ru.kaplaan.web.dto.response.jwt.JwtResponse
import java.util.*

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val emailService: EmailService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder
) : AuthService {

    override fun register(user: User) {
        checkRegistration(user)

        val hashedPassword: String = passwordEncoder.encode(user.password)
        val activationCode = UUID.randomUUID().toString().replace("-", "")

        user.password = hashedPassword
        user.activationCode = activationCode

        emailService.activateUserByEmail(user.email, user.username, activationCode)

        userRepository.save(user)
    }

    override fun authenticate(userIdentification: UserIdentification): JwtResponse {
        val user = getUserByUsernameOrEmail(userIdentification)

        val accessToken = jwtService.generateJwtAccessToken(user)
        val refreshToken = jwtService.generateJwtRefreshToken(user)

        return JwtResponse(accessToken, refreshToken)
    }

    private fun authenticateByUsername(userIdentification: UserIdentification): User? {
        return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    userIdentification.usernameOrEmail,
                    userIdentification.password
                )
            )
            authentication.principal as User
        } catch (e: BadCredentialsException) {
            null
        }
    }

    private fun authenticateByEmail(userIdentification: UserIdentification): User? {
        val user = userRepository.findByEmail(userIdentification.usernameOrEmail)

        return if (user == null ||
            passwordEncoder.encode(userIdentification.password) != user.password) null
        else user
    }

    override fun activateAccount(code: String) {
        val user = userRepository.getUserByActivationCode(code) ?: throw NotFoundUserByActivationCode()
        user.activationCode = null
        user.activated = true
        userRepository.save(user)
    }

    // TODO: Полностью переделать восстановление пароля
    override fun passwordRecovery(userIdentification: UserIdentification) {
        val user = getUserByUsernameOrEmail(userIdentification)
        val activationCode = UUID.randomUUID().toString().replace("-", "")
        user.activationCode = activationCode
        emailService.recoveryPasswordByEmail(user.email, user.username, activationCode)
        userRepository.save(user)
    }

    private fun getUserByUsernameOrEmail(userIdentification: UserIdentification): User {
        return authenticateByUsername(userIdentification) ?:
            authenticateByEmail(userIdentification) ?:
                throw UserNotFoundException("Пользователь с таким логином или паролем не найден")
    }

    private fun saveRefreshToken(login: String, token: String) {
        val refreshToken = RefreshToken(login, token)
        refreshTokenRepository.save(refreshToken)
    }

    private fun checkRegistration(user: User) {
        if (userRepository.findByUsername(user.username) != null) throw UserAlreadyRegisteredException("Пользователь с таким логином или паролем уже существует")
    }
}
