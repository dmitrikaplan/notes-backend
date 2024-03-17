package ru.kaplaan.authserver.domain.config

import org.springframework.security.authentication.*
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import ru.kaplaan.authserver.domain.entity.User
import ru.kaplaan.authserver.repository.UserRepository

class EmailAuthenticationProvider(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): AbstractUserDetailsAuthenticationProvider() {
    override fun authenticate(authentication: Authentication): Authentication {
        val userDetails = retrieveUser(authentication.name, authentication as UsernamePasswordAuthenticationToken)

        return UsernamePasswordAuthenticationToken.authenticated(
            userDetails,
            authentication.credentials as String,
            userDetails.authorities
        )
    }

    override fun additionalAuthenticationChecks(
        userDetails: UserDetails,
        authentication: UsernamePasswordAuthenticationToken,
    ) {

        when{
            !passwordEncoder.matches(authentication.credentials as String, userDetails.password) -> throw BadCredentialsException("Неверный пароль!")

            (userDetails as User).email != authentication.name -> throw BadCredentialsException("Неверный username!")

            !userDetails.isEnabled -> throw AuthenticationServiceException("Аккаунт не доступен!")

            !userDetails.isAccountNonLocked -> throw LockedException("Аккаунт заблокирован!")

            !userDetails.isCredentialsNonExpired -> throw CredentialsExpiredException("Срок действия данных пользователя истек!")
        }
    }

    override fun retrieveUser(username: String, authentication: UsernamePasswordAuthenticationToken): UserDetails {
        return userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("Не найден пользователь по email")
    }
}