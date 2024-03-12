package ru.kaplaan.authserver.service


import org.springframework.stereotype.Service

@Service
interface EmailService {
    fun activateUserByEmail(emailTo: String, login: String, activationCode: String)

    fun recoveryPasswordByEmail(emailTo: String, login: String, activationCode: String)
}
