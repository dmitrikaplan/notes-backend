package com.example.service.impl

import com.example.service.EmailService
import com.example.utils.enums.KindsOfEmailMessages
import com.example.utils.enums.KindsOfSubjects
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.nio.charset.StandardCharsets

@Service
class EmailServiceImpl @Autowired constructor(
    private val mailSender: JavaMailSender,
    private val springTemplateEngine: SpringTemplateEngine
) : EmailService {

    @Value("\${kittynotes.host}")
    private var host: String? = null

    @Value("\${spring.mail.username}")
    private var mail: String? = null

    override fun activateUserByEmail(emailTo: String, login: String, activationCode: String) {
        val templateLocation = KindsOfEmailMessages.REGISTRATION_EMAIL.pathOfTemplate
        val subject = KindsOfSubjects.SUBJECT_FOR_REGISTRATION.subject
        val endpoint = "activation"
        val context = Context()
        context.setVariable("login", login)
        context.setVariable("activationLink", generateActivationLink(activationCode, host, endpoint))
        context.setVariable("subject", subject)
        sendEmail(emailTo, login, context, subject, templateLocation)
    }

    override fun recoveryPasswordByEmail(emailTo: String, login: String, activationCode: String) {
        val templateLocation = KindsOfEmailMessages.RECOVERY_EMAIL.pathOfTemplate
        val subject = KindsOfSubjects.SUBJECT_FOR_PASSWORD_RECOVERY.subject
        val endpoint = "recovery"
        val context = Context()
        context.setVariable("login", login)
        context.setVariable("activationLink", generateActivationLink(activationCode, host, endpoint))
        context.setVariable("subject", subject)
        sendEmail(emailTo, login, context, subject, templateLocation)
    }

    private fun sendEmail(emailTo: String, login: String, context: Context, subject: String, templateLocation: String) {
        val mailMessage = mailSender.createMimeMessage()
        val mimeMessageHelper = MimeMessageHelper(
            mailMessage,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name()
        )
        val emailContent = springTemplateEngine.process(templateLocation, context)
        mimeMessageHelper.setFrom(mail)
        mimeMessageHelper.setSubject(subject)
        mimeMessageHelper.setTo(emailTo)
        mimeMessageHelper.setText(emailContent, true)
        mailSender.send(mailMessage)
    }

    private fun generateActivationLink(activationCode: String, host: String?, endpoint: String): String {
        return String.format("%s/%s/%s", host, endpoint, activationCode)
    }


    fun getHost() =
        host

    fun setHost(host: String){
        this.host = host
    }

    fun getMail() =
        mail

    fun setMail(mail: String){
        this.mail = mail
    }
}
