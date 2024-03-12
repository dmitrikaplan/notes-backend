package ru.kaplaan.notes.domain.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import ru.kaplaan.domain.domain.config.DomainConfig

@Configuration
@Import(value = [DomainConfig::class])
@EnableJpaRepositories(basePackages = ["ru.kaplaan.notes"])
@EntityScan(basePackages = ["ru.kaplaan.notes"])
@ComponentScan(basePackages = ["ru.kaplaan.notes"])
class NotesConfig