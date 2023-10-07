package ru.kaplaan.domain.config


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
    fun getMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = username
        mailSender.password = password
        mailSender.protocol = protocol
        val properties = mailSender.javaMailProperties
        properties.setProperty("mail.transport.protocol", protocol)
        properties.setProperty("mail.debug", debug)

        return mailSender
    }


    fun getHost() =
        host


    fun getUsername() =
        username


    fun getPassword() =
        password


    fun getPort() =
        port


    fun getProtocol() =
        protocol


    fun getDebug() =
        debug

}
