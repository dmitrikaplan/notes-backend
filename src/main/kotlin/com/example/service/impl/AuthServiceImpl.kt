package com.example.service.impl

import com.example.repository.RefreshTokenRepository
import com.example.repository.UserRepository
import com.example.service.AuthService
import com.example.service.CryptoService
import com.example.service.EmailService
import com.example.service.ValidationService
import com.example.utils.JwtProvider
import com.example.utils.exceptions.NotFoundUserByActivationCode
import com.example.utils.exceptions.NotValidEmailException
import com.example.utils.exceptions.UserAlreadyRegisteredException
import com.example.utils.exceptions.UserNotFoundException
import com.example.utils.model.JwtResponse
import com.example.utils.model.entities.RefreshToken
import com.example.utils.model.entities.User
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val emailService: EmailService,
    private val cryptoService: CryptoService,
    private val jwtProvider: JwtProvider,
    private val validationService: ValidationService
) : AuthService {

    override fun login(user: User): JwtResponse {
        checkLogin(user)
        val accessToken = jwtProvider.generateJwtAccessToken(user)
        val refreshToken = jwtProvider.generateJwtRefreshToken(user)
        saveRefreshToken(user.getLogin()!!, refreshToken)
        return JwtResponse(accessToken, refreshToken)
    }

    override fun registration(user: User) {
        checkRegistration(user)
        val hashedPassword: String = cryptoService.doHash(user)
        val activationCode = UUID.randomUUID().toString().replace("-", "")
        user.setPassword(hashedPassword)
        user.setActivationCode(activationCode)
        emailService.activateUserByEmail(user.getEmail()!!, user.getLogin()!!, activationCode)
        userRepository.save(user)
    }

    override fun activateAccount(code: String) {
        val user = userRepository.getUserByActivationCode(code) ?: throw NotFoundUserByActivationCode()
        user.setActivationCode(null)
        user.setActivated(true)
        userRepository.save(user)
    }

    override fun passwordRecovery(user: User) {
        val u = getUserByLoginOrEmail(user)
        if (!validationService.validateEmail(u.getEmail()!!)) throw NotValidEmailException()
        val activationCode = UUID.randomUUID().toString().replace("-", "")
        user.setActivationCode(activationCode)
        emailService.recoveryPasswordByEmail(u.getEmail()!!, u.getLogin()!!, activationCode)
        userRepository.save(user)
    }

    private fun getUserByLoginOrEmail(user: User): User {
        if (user.getLogin() == null) {
            val u = userRepository.getUserByEmail(user.getEmail()!!)
                ?: throw UserNotFoundException("Пользователь с такой почтой не найден")
            user.setLogin(u.getLogin()!!)
        } else {
            val u = userRepository.getUserByLogin(user.getLogin()!!)
                ?: throw UserNotFoundException("Пользователь с таким логином не найден")
            user.setEmail(u.getEmail()!!)
        }
        return user
    }

    private fun saveRefreshToken(login: String, token: String) {
        val refreshToken = RefreshToken(login, token)
        refreshTokenRepository.save(refreshToken)
    }

    private fun checkLogin(user: User) {
        validationService.validateLogin(user)
        val u = getUserByLoginOrEmail(user)
        val hashedPassword: String = cryptoService.getHash(user)
        if (!userRepository.existsUserByLoginAndPasswordAndEmailAndActivated(
                u.getLogin()!!,
                hashedPassword,
                u.getEmail()!!,
                true
            )
        ) throw UserNotFoundException("Пользователь с данным логином и паролем не найден")
    }

    private fun checkRegistration(user: User) {
        validationService.validateRegistration(user)
        if (userRepository.existsUserByLoginOrEmailAndActivated(
                user.getLogin()!!,
                user.getEmail()!!,
                true
            )
        ) throw UserAlreadyRegisteredException("Пользователь с таким логином или паролем уже существует")
    }
}
