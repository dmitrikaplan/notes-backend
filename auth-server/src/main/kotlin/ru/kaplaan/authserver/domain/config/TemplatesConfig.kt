package ru.kaplaan.authserver.domain.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

@Configuration
class TemplatesConfig : WebMvcConfigurer {
    @Bean
    fun emailTemplateResolver(): ClassLoaderTemplateResolver =
        ClassLoaderTemplateResolver().apply {
            prefix = "/templates/email/"
            suffix = ".html"
            characterEncoding = "UTF-8"
        }

    @Bean
    fun defaultTemplateResolver(): ClassLoaderTemplateResolver =
        ClassLoaderTemplateResolver().apply {
            prefix = "/templates/"
            suffix = ".html"
            characterEncoding = "UTF-8"
        }


    @Bean
    fun templateEngine(): SpringTemplateEngine =
        SpringTemplateEngine().apply {
            addTemplateResolver(emailTemplateResolver())
            addTemplateResolver(defaultTemplateResolver())
        }
}
