package ru.kaplaan.api.domain.auth

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.client.RestClient

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
            val restClient = it.getSharedObject(RestClient::class.java)
            it.addFilterBefore(
                JwtAuthenticationFilter(
                    jwtAuthenticationConverter = jwtAuthenticationConverter,
                    authenticationEntryPoint = authenticationEntryPoint,
                    restClient = restClient
                    ),
                    UsernamePasswordAuthenticationFilter::class.java
            )
        } ?: throw RuntimeException()

    }
}