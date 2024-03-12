package ru.kaplaan.domain.domain.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import ru.kaplaan.domain.repository.UserRepository

@Configuration
class ApplicationConfig(
    private val userRepository: UserRepository
) {

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService {
            userRepository.findByUsername(it)
                ?: throw UsernameNotFoundException(null)
        }
    }
}