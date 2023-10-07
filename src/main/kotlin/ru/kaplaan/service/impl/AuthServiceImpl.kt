package ru.kaplaan.service.impl

import org.springframework.stereotype.Service
import ru.kaplaan.domain.entity.RefreshToken
import ru.kaplaan.domain.entity.User
import ru.kaplaan.domain.exception.user.*
import ru.kaplaan.domain.jwt.JwtProvider
import ru.kaplaan.domain.user.UserIdentification
import ru.kaplaan.repository.RefreshTokenRepository
import ru.kaplaan.repository.UserRepository
import ru.kaplaan.service.AuthService
import ru.kaplaan.service.CryptoService
import ru.kaplaan.service.EmailService
import ru.kaplaan.web.dto.response.jwt.JwtResponse
import java.util.*

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val emailService: EmailService,
    private val cryptoService: CryptoService,
    private val jwtProvider: JwtProvider,
) : AuthService {

    override fun registration(user: User) {
        checkRegistration(user)
        val hashedPassword: String = cryptoService.doHash(user)
        val activationCode = UUID.randomUUID().toString().replace("-", "")
        user.password = hashedPassword
        user.activationCode = activationCode
        emailService.activateUserByEmail(user.email, user.login, activationCode)
        userRepository.save(user)
    }

    override fun login(userIdentification: UserIdentification): JwtResponse {
        val user = checkLogin(userIdentification)
        val accessToken = jwtProvider.generateJwtAccessToken(user)
        val refreshToken = jwtProvider.generateJwtRefreshToken(user)
        saveRefreshToken(user.login, refreshToken)
        return JwtResponse(accessToken, refreshToken)
    }

    override fun activateAccount(code: String) {
        val user = userRepository.getUserByActivationCode(code) ?: throw NotFoundUserByActivationCode()
        user.activationCode = null
        user.activated = true
        userRepository.save(user)
    }

    // TODO: Полностью переделать восстановление пароля
    override fun passwordRecovery(loginOrEmail: String) {
        val user = getUserByLoginOrEmail(loginOrEmail)
        val activationCode = UUID.randomUUID().toString().replace("-", "")
        user.activationCode = activationCode
        emailService.recoveryPasswordByEmail(user.email, user.login, activationCode)
        userRepository.save(user)
    }

    private fun getUserByLoginOrEmail(loginOrEmail: String): User {
        return userRepository.getUserByLogin(loginOrEmail)
            ?: (userRepository.getUserByEmail(loginOrEmail)
                ?: throw UserNotFoundException("Пользователь с такой почтой или логином не найден"))
    }

    private fun saveRefreshToken(login: String, token: String) {
        val refreshToken = RefreshToken(login, token)
        refreshTokenRepository.save(refreshToken)
    }

    fun checkLogin(userIdentification: UserIdentification): User {
        val user = getUserByLoginOrEmail(userIdentification.loginOrEmail)
        val hashedPassword: String = cryptoService.getHash(user.login, userIdentification.password)
        if (!userRepository.existsUserByLoginAndPasswordAndEmailAndActivated(
                user.login,
                hashedPassword,
                user.email,
                true
            )
        ) throw UserNotFoundException("Пользователь с данным логином и паролем не найден")
        return user
    }

    private fun checkRegistration(user: User): User {
        validateActivationCode(user.activationCode, false)
        validateActivated(user.activated!!, false)
        if (userRepository.existsUserByLoginOrEmailAndActivated(
                user.login,
                user.email,
                true
            )
        ) throw UserAlreadyRegisteredException("Пользователь с таким логином или паролем уже существует")
        return user
    }

    private fun validateActivationCode(activationCode: String?, mustExist: Boolean){
        if((mustExist && activationCode == null) || (!mustExist && activationCode != null))
            throw UnexpectedActivationCodeException()
    }

    private fun validateActivated(activated: Boolean, mustBe: Boolean){
        if(activated != mustBe)
            throw UnexpectedActivatedException()
    }
}
