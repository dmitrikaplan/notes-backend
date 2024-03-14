package ru.kaplaan.notes.domain.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.kaplaan.domain.domain.config.JwtConfigurer
import ru.kaplaan.domain.jwt.JwtAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val authenticationConverter: AuthenticationConverter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic {
                it.disable()
            }
            .csrf {
                it.disable()
            }
            .cors {
                it.disable()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .also {
                it.setSharedObject(AuthenticationConverter::class.java, authenticationConverter)
            }
            .apply(JwtConfigurer())

        return http.build()
    }

}
