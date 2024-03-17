package ru.kaplaan.authserver.domain.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.kaplaan.authserver.repository.UserRepository

@Configuration
class AuthServiceConfig(
    private val userRepository: UserRepository
) {


    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager



    @Bean
    fun userDetailsService(): UserDetailsService =
        UserDetailsService {
            userRepository.findByUsername(it)
                ?: throw UsernameNotFoundException("Не найден пользователь по username!")
        }


    @Bean
    fun authenticationProvider(): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService())
            setPasswordEncoder(passwordEncoder())
        }


    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()


}