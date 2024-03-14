package ru.kaplaan.authserver.domain.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationConverter
import ru.kaplaan.domain.domain.config.JwtConfigurer

@Configuration
@EnableWebSecurity
class SecurityConfig(
    //private val authenticationProvider: AuthenticationProvider,
    private val authenticationConverter: AuthenticationConverter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic{
                it.disable()
            }
            .csrf{
                it.disable()
            }
            .sessionManagement{
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests{
                it.requestMatchers(HttpMethod.GET, "/api/v1/auth/activation/{code}").permitAll()
                it.requestMatchers(HttpMethod.GET, "/swagger-ui/*").permitAll()
                it.requestMatchers(HttpMethod.GET, "/v3/api-docs/*").permitAll()
                it.requestMatchers(HttpMethod.GET, "/v3/api-docs").permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/v1/auth/registration/user").permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh").permitAll()
                it.anyRequest().authenticated()
            }
            .also {
                it.setSharedObject(AuthenticationConverter::class.java, authenticationConverter)
            }
            .apply(JwtConfigurer())

        return http.build()
    }


}
