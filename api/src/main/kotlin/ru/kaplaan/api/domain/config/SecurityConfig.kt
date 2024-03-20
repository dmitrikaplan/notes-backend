package ru.kaplaan.api.domain.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.reactive.function.client.WebClient
import ru.kaplaan.api.domain.auth.JwtAuthenticationFilter

@Configuration
@EnableWebFluxSecurity
class SecurityConfig (
    private val webClient: WebClient,
    private val jwtAuthenticationConverter: ServerAuthenticationConverter
) {

    @Value("\${auth-server.base-url}")
    lateinit var baseUrl: String

    @Value("\${auth-server.endpoint.authentication}")
    lateinit var authenticationEndpoint: String

    @Bean
    fun securityWebFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain =
        httpSecurity
            .httpBasic {
                it.disable()
            }
            .csrf {
                it.disable()
            }
            .cors {
                it.disable()
            }
            .authorizeExchange {
                it.pathMatchers(HttpMethod.POST, "api/v1/auth/registration/user").permitAll()
                it.pathMatchers(HttpMethod.POST, "api/v1/auth/login").permitAll()
                it.pathMatchers(HttpMethod.GET, "api/v1/auth/activation/**").permitAll()
                it.pathMatchers(HttpMethod.POST, "api/v1/auth/recovery").permitAll()
                it.pathMatchers(HttpMethod.POST, "api/v1/auth/refresh").permitAll()
                it.pathMatchers(HttpMethod.GET, "/test").permitAll()
                it.anyExchange().authenticated()
            }
            .addFilterBefore(JwtAuthenticationFilter("$baseUrl$authenticationEndpoint", jwtAuthenticationConverter, webClient), SecurityWebFiltersOrder.AUTHENTICATION)
            .build()


}
