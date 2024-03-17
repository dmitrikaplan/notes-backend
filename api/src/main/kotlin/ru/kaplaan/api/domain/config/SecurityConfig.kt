package ru.kaplaan.api.domain.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.web.client.RestClient
import ru.kaplaan.api.domain.auth.JwtConfigurer

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val authenticationConverter: AuthenticationConverter,
    private val restClient: RestClient
) {

    @Value("\${auth-server.base-url}")
    lateinit var baseUrl: String

    @Value("\${auth-server.endpoint.authentication}")
    lateinit var authenticationEndpoint: String

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
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
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.POST, "api/v1/auth/registration/user").permitAll()
                it.requestMatchers(HttpMethod.POST, "api/v1/auth/login").permitAll()
                it.requestMatchers(HttpMethod.GET, "api/v1/auth/activation/**").permitAll()
                it.requestMatchers(HttpMethod.POST, "api/v1/auth/recovery").permitAll()
                it.requestMatchers(HttpMethod.POST, "api/v1/auth/refresh").permitAll()
                it.anyRequest().authenticated()
            }
            .also {
                it.setSharedObject(AuthenticationConverter::class.java, authenticationConverter)
                it.setSharedObject(RestClient::class.java, restClient)
                it.setSharedObject(String::class.java, "$baseUrl$authenticationEndpoint")
            }
            .apply(JwtConfigurer())

        return httpSecurity.build()
    }

}
