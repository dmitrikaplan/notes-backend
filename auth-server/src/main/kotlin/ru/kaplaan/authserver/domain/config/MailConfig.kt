package ru.kaplaan.authserver.domain.config


import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailConfig {
    @Value("\${spring.mail.host}")
    private lateinit var host: String

    @Value("\${spring.mail.username}")
    private lateinit var username: String

    @Value("\${spring.mail.password}")
    private lateinit var password: String

    @Value("\${spring.mail.port}")
    private var port = 0

    @Value("\${spring.mail.protocol}")
    private lateinit var protocol: String

    @Value("\${mail.debug}")
    private lateinit var debug: String

    @Bean
    fun getMailSender(): JavaMailSender =
        JavaMailSenderImpl().apply {
            host = this@MailConfig.host
            port = this@MailConfig.port
            username = this@MailConfig.username
            password = this@MailConfig.password
            protocol = this@MailConfig.protocol
            javaMailProperties.apply {
                setProperty("mail.transport.protocol", protocol)
                setProperty("mail.debug", debug)
            }
        }


}
