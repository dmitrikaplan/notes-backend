package ru.kaplaan.authserver.domain.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import ru.kaplaan.domain.domain.config.DomainConfig

@Configuration
@Import(value = [DomainConfig::class])
@EnableJpaRepositories(basePackages = ["ru.kaplaan.authserver"])
@EntityScan(basePackages = ["ru.kaplaan.authserver"])
@ComponentScan(basePackages = ["ru.kaplaan.authserver"])
class AuthServerConfig