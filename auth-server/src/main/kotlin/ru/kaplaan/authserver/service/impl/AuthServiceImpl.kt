package ru.kaplaan.authserver.service.impl

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.kaplaan.authserver.domain.entity.RefreshToken
import ru.kaplaan.authserver.domain.exception.NotFoundUserByActivationCodeException
import ru.kaplaan.authserver.domain.user.UserIdentification
import ru.kaplaan.authserver.repository.RefreshTokenRepository
import ru.kaplaan.authserver.service.AuthService
import ru.kaplaan.authserver.service.EmailService
import ru.kaplaan.authserver.web.dto.response.jwt.JwtResponse
import ru.kaplaan.domain.domain.exception.UserAlreadyRegisteredException
import ru.kaplaan.domain.domain.exception.UserNotFoundException
import ru.kaplaan.domain.domain.user.User
import ru.kaplaan.domain.jwt.JwtService
import ru.kaplaan.domain.repository.UserRepository
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

    override fun registerUser(user: User) {

        checkRegistration(user)

        val activationCode = UUID.randomUUID().toString().replace("-", "")

        user.apply {
            this.password = passwordEncoder.encode(user.password)
            this.activationCode = activationCode
        }

        emailService.activateUserByEmail(user.email, user.username, activationCode)

        userRepository.save(user)
    }

    override fun registerAdmin(user: User) {
        TODO("Not yet implemented")
    }

    override fun authenticate(userIdentification: UserIdentification): JwtResponse {
        val user = getUserByUsernameOrEmail(userIdentification)

        val accessToken = jwtService.generateJwtAccessToken(user)
        val refreshToken = jwtService.generateJwtRefreshToken(user)

        return JwtResponse(accessToken, refreshToken)
    }

    private fun authenticateByUsername(userIdentification: UserIdentification): User? {
        return try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    userIdentification.usernameOrEmail,
                    userIdentification.password
                )
            ).principal as User

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
        userRepository.getUserByActivationCode(code)?.apply {

            activationCode = null
            activated = true
            userRepository.save(this)

        } ?: throw NotFoundUserByActivationCodeException()

    }

    // TODO: Полностью переделать восстановление пароля
    override fun passwordRecovery(userIdentification: UserIdentification) {
        val activationCode = UUID.randomUUID().toString().replace("-", "")
        val user = getUserByUsernameOrEmail(userIdentification).apply {
            this.activationCode = activationCode
        }
        emailService.recoveryPasswordByEmail(user.email, user.username, activationCode)

        userRepository.save(user)
    }

    override fun getNewRefreshToken(jwtToken: String): JwtResponse {
        TODO()
    }

    private fun getUserByUsernameOrEmail(userIdentification: UserIdentification): User {
        return authenticateByUsername(userIdentification) ?:
            authenticateByEmail(userIdentification) ?:
                throw UserNotFoundException("Пользователь с таким логином или паролем не найден")
    }

    private fun saveRefreshToken(login: String, token: String) =
        RefreshToken(login, token).let{ refreshToken ->
            refreshTokenRepository.save(refreshToken)
        }

    private fun checkRegistration(user: User) =
        userRepository.findByUsername(user.username)?.let {
            throw UserAlreadyRegisteredException("Пользователь с таким логином или паролем уже существует")
        }
}
