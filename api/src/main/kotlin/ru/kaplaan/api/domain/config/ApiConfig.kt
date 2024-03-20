package ru.kaplaan.api.domain.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import reactor.core.publisher.Mono
import ru.kaplaan.api.domain.auth.JwtService


@Configuration
class ApiConfig(
    private val jwtService: JwtService,
) {

    @Bean
    fun jwtAuthenticationConverter() =
        ServerAuthenticationConverter { serverWebExchange ->

            serverWebExchange.request.headers[HttpHeaders.AUTHORIZATION]?.forEach {
                val header = it.trim()

                if(!header.startsWith("Bearer "))
                    return@forEach

                val jwtToken = header.substring(7)

                if(!jwtService.isValidAccessToken(jwtToken))
                    return@forEach

                val username = jwtService.extractUsernameFromAccessToken(jwtToken)
                val password = jwtService.extractPasswordFromAccessToken(jwtToken)

                return@ServerAuthenticationConverter Mono.just(UsernamePasswordAuthenticationToken
                    .unauthenticated(username, password))
            }

            return@ServerAuthenticationConverter Mono.empty()

        }
}