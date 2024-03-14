package ru.kaplaan.domain.domain.config

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.kaplaan.domain.jwt.JwtAuthenticationFilter
import ru.kaplaan.domain.jwt.JwtService

class JwtConfigurer: AbstractHttpConfigurer<JwtConfigurer, HttpSecurity>() {

    private var authenticationEntryPoint =
        AuthenticationEntryPoint{ request, response, authException ->
            response.apply {
                addHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer ")
                sendError(HttpStatus.UNAUTHORIZED.value(), authException.message)
            }

    }

    override fun init(builder: HttpSecurity?) {
        builder?.let {
            builder.exceptionHandling{
                it.authenticationEntryPoint(this.authenticationEntryPoint)
            }
        }
    }
    override fun configure(builder: HttpSecurity?) {
        builder?.let {
            val jwtAuthenticationConverter = it.getSharedObject(AuthenticationConverter::class.java)
            val authenticationManager = it.getSharedObject(AuthenticationManager::class.java)
            it.addFilterBefore(
                JwtAuthenticationFilter(
                    jwtAuthenticationConverter = jwtAuthenticationConverter,
                    authenticationManager = authenticationManager,
                    authenticationEntryPoint = this.authenticationEntryPoint
                    ),
                    UsernamePasswordAuthenticationFilter::class.java
            )
        } ?: throw RuntimeException()

    }
}