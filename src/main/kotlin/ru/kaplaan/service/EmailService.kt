package ru.kaplaan.service

import jakarta.mail.MessagingException
import org.springframework.stereotype.Service

@Service
interface EmailService {
    fun activateUserByEmail(emailTo: String, login: String, activationCode: String)

    fun recoveryPasswordByEmail(emailTo: String, login: String, activationCode: String)
}
