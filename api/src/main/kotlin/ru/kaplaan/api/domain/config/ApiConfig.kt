package ru.kaplaan.api.domain.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.authentication.AuthenticationConverter
import ru.kaplaan.api.domain.auth.JwtService


@Configuration
class ApiConfig(
    private val jwtService: JwtService,
) {

    @Bean
    fun jwtAuthenticationConverter() =
        AuthenticationConverter{ request ->

            request.getHeader(HttpHeaders.AUTHORIZATION)?.let {

                val header = it.trim()
                if(!header.startsWith("Bearer "))
                    return@AuthenticationConverter null

                val jwtToken = header.substring(7)

                if(!jwtService.isValidAccessToken(jwtToken))
                    return@AuthenticationConverter null

                val username = jwtService.extractUsernameFromAccessToken(jwtToken)
                val password = jwtService.extractPasswordFromAccessToken(jwtToken)

                UsernamePasswordAuthenticationToken
                    .unauthenticated(username, password)

            } ?: return@AuthenticationConverter null

        }
}