package com.example.config

import com.example.utils.filters.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val jwtFilter: JwtFilter
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic().disable()
            .csrf().disable()
            .cors().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET, "/api/v1/auth/activation/{code}", "/api/v1/test").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/registration").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

}
