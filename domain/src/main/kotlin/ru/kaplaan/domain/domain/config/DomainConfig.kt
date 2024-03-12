package ru.kaplaan.domain.domain.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["ru.kaplaan.domain"])
@EntityScan(basePackages = ["ru.kaplaan.domain"])
@ComponentScan(basePackages = ["ru.kaplaan.domain"])
class DomainConfig