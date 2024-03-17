package ru.kaplaan.api.domain.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import ru.kaplaan.api.domain.auth.JwtService
import ru.kaplaan.api.domain.user.User


@Configuration
class ApiConfig(
    private val jwtService: JwtService,
    private val restClient: RestClient
) {

    @Value("\${auth-server.base-url}")
    lateinit var baseUrl: String

    @Value("\${auth-server.endpoint.get-by-username}")
    lateinit var getByUsernameEndpoint: String


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