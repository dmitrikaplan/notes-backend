package ru.kaplaan.authserver.service.impl

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.authserver.domain.entity.RefreshToken
import ru.kaplaan.authserver.domain.entity.User
import ru.kaplaan.authserver.domain.exception.refresh_token.RefreshTokenExpiredException
import ru.kaplaan.authserver.domain.exception.refresh_token.RefreshTokenNotFoundException
import ru.kaplaan.authserver.domain.exception.user.NotFoundUserByActivationCodeException
import ru.kaplaan.authserver.domain.exception.user.UserAlreadyRegisteredException
import ru.kaplaan.authserver.domain.exception.user.UserNotFoundException
import ru.kaplaan.authserver.domain.user.UserIdentification
import ru.kaplaan.authserver.repository.RefreshTokenRepository
import ru.kaplaan.authserver.repository.UserRepository
import ru.kaplaan.authserver.service.AuthService
import ru.kaplaan.authserver.service.EmailService
import ru.kaplaan.authserver.service.JwtService
import ru.kaplaan.authserver.web.dto.response.JwtResponse
import java.util.*

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val emailService: EmailService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
) : AuthService {

    override fun register(user: User) {

        checkRegistration(user)

        val activationCode = UUID.randomUUID().toString().replace("-", "")

        user.apply {
            this.password = passwordEncoder.encode(user.password)
            this.activationCode = activationCode
        }

        emailService.activateUserByEmail(user.email, user.username, activationCode)

        userRepository.save(user)
    }

    override fun authenticate(userIdentification: UserIdentification): JwtResponse {
        val user = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken
                .unauthenticated(userIdentification.usernameOrEmail, userIdentification.password)
        ).principal as User

        val accessToken = jwtService.generateJwtAccessToken(user)
        val refreshToken = getRefreshToken(user)

        return JwtResponse(accessToken, refreshToken)
    }

    override fun authenticate(authentication: Authentication): Authentication =
        authenticationManager.authenticate(authentication)


    override fun activateAccount(code: String) {
        userRepository.getUserByActivationCode(code)?.apply {

            activationCode = null
            activated = true
            userRepository.save(this)

        } ?: throw NotFoundUserByActivationCodeException()

    }

    // TODO: Полностью переделать восстановление пароля
    override fun passwordRecovery(userIdentification: UserIdentification) {
        TODO()
    }

    @Transactional
    override fun refresh(token: String): JwtResponse {

        if(!jwtService.isValidRefreshToken(token)){
            refreshTokenRepository.deleteByToken(token)
            throw RefreshTokenExpiredException()
        }

        val accessToken = refreshTokenRepository.findRefreshTokenByToken(token)?.user?.let { user ->
            jwtService.generateJwtAccessToken(user)
        } ?: throw RefreshTokenNotFoundException()

        return JwtResponse(accessToken, token)
    }

    override fun getUserByUsername(username: String): User =
        userRepository.findByUsername(username)
            ?: throw UserNotFoundException(null)


    private fun getRefreshToken(user: User): String{

        val refreshToken = refreshTokenRepository.findRefreshTokenByUser(user)
            ?: return saveRefreshToken(user)

        if(!jwtService.isValidRefreshToken(refreshToken.token))
            return updateRefreshToken(refreshToken, user)

        return refreshToken.token
    }

    private fun updateRefreshToken(refreshToken: RefreshToken, userDetails: UserDetails): String {
        val token = jwtService.generateJwtRefreshToken(userDetails as User)
        refreshToken.token = token
        refreshTokenRepository.save(refreshToken)
        return token
    }

    private fun saveRefreshToken(user: User): String {
        val token =  jwtService.generateJwtRefreshToken(user)
        val refreshToken = RefreshToken().apply {
            this.token = token
            this.user = user
        }
        refreshTokenRepository.save(refreshToken)
        return token
    }

    private fun checkRegistration(user: User) =
        userRepository.findByUsername(user.username)?.let {
            throw UserAlreadyRegisteredException("Пользователь с таким логином или паролем уже существует")
        }
}
