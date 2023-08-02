package com.example.config


import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailConfig {
    @Value("\${spring.mail.host}")
    private var host: String? = null

    @Value("\${spring.mail.username}")
    private var username: String? = null

    @Value("\${spring.mail.password}")
    private var password: String? = null

    @Value("\${spring.mail.port}")
    private var port = 0

    @Value("\${spring.mail.protocol}")
    private var protocol: String? = null

    @Value("\${mail.debug}")
    private var debug: String? = null

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

    fun setHost(host: String){
        this.host = host
    }

    fun getUsername() =
        username

    fun setUsername(username: String){
        this.username = username
    }

    fun getPassword() =
        password

    fun setPassword(password: String){
        this.password = password
    }

    fun getPort() =
        port

    fun setPort(port: Int){
        this.port = port
    }

    fun getProtocol() =
        protocol

    fun setProtocol(protocol: String){
        this.protocol = protocol
    }

    fun getDebug() =
        debug

    fun setDebug(debug: String){
        this.debug = debug
    }
}
