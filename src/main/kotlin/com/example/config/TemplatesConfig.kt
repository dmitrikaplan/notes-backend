package com.example.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

@Configuration
class TemplatesConfig : WebMvcConfigurer {
    @Bean
    fun emailTemplateResolver(): ClassLoaderTemplateResolver {
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "/templates/email/"
        templateResolver.suffix = ".html"
        templateResolver.characterEncoding = "UTF-8"
        return templateResolver
    }

    @Bean
    fun templateResolver(): ClassLoaderTemplateResolver {
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "/templates/"
        templateResolver.suffix = ".html"
        templateResolver.characterEncoding = "UTF-8"
        return templateResolver
    }

    @Bean
    fun templateEngine(): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.addTemplateResolver(emailTemplateResolver())
        templateEngine.addTemplateResolver(templateResolver())
        return templateEngine
    }
}
