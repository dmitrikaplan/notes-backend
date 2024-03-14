package ru.kaplaan.domain.domain.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationConverter
import ru.kaplaan.domain.jwt.JwtService
import ru.kaplaan.domain.repository.UserRepository

@Configuration
class ApplicationConfig(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {

    @Bean
    fun userDetailsService(): UserDetailsService =
        UserDetailsService {
            userRepository.findByUsername(it)
                ?: throw UsernameNotFoundException(null)
        }


    @Bean
    fun jwtAuthenticationConverter() =
        AuthenticationConverter{ request ->

            request.getHeader(HttpHeaders.AUTHORIZATION)?.let {

                val header = it.trim()
                if(!header.startsWith("Bearer "))
                    return@AuthenticationConverter null

                val jwtToken = header.substring(7)

                val username = jwtService.extractUsernameFromAccessToken(jwtToken)
                val password = jwtService.extractPasswordFromAccessToken(jwtToken)

                UsernamePasswordAuthenticationToken
                    .unauthenticated(username, password)

            } ?: return@AuthenticationConverter null

        }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager


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