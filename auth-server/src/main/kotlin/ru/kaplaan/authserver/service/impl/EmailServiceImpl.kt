package ru.kaplaan.authserver.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import ru.kaplaan.authserver.domain.email.KindsOfEmailMessages
import ru.kaplaan.authserver.domain.email.KindsOfSubjects
import ru.kaplaan.authserver.service.EmailService
import java.nio.charset.StandardCharsets

@Service
class EmailServiceImpl @Autowired constructor(
    private val mailSender: JavaMailSender,
    private val springTemplateEngine: SpringTemplateEngine
) : EmailService {

    @Value("\${kittynotes.host}")
    private lateinit var host: String

    @Value("\${spring.mail.username}")
    private lateinit var mail: String

    override fun activateUserByEmail(emailTo: String, login: String, activationCode: String) {
        val templateLocation = KindsOfEmailMessages.REGISTRATION_EMAIL.pathOfTemplate
        val subject = KindsOfSubjects.SUBJECT_FOR_REGISTRATION.subject
        val endpoint = "activation"
        val context = Context().apply{
            setVariable("username", login)
            setVariable("activationLink", generateActivationLink(activationCode, host, endpoint))
            setVariable("subject", subject)
        }

        sendEmail(emailTo, login, context, subject, templateLocation)

    }

    override fun recoveryPasswordByEmail(emailTo: String, login: String, activationCode: String) {
        val templateLocation = KindsOfEmailMessages.RECOVERY_EMAIL.pathOfTemplate
        val subject = KindsOfSubjects.SUBJECT_FOR_PASSWORD_RECOVERY.subject
        val endpoint = "recovery"
        val context = Context().apply {
            setVariable("username", login)
            setVariable("activationLink", generateActivationLink(activationCode, host, endpoint))
            setVariable("subject", subject)
        }
        sendEmail(emailTo, login, context, subject, templateLocation)
    }

    private fun sendEmail(emailTo: String, login: String, context: Context, subject: String, templateLocation: String) {
        val mailMessage = mailSender.createMimeMessage()
        val emailContent = springTemplateEngine.process(templateLocation, context)

        MimeMessageHelper(
            mailMessage,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name()
        ).apply {
            setFrom(mail)
            setSubject(subject)
            setTo(emailTo)
            setText(emailContent, true)
        }

        mailSender.send(mailMessage)
    }

    private fun generateActivationLink(activationCode: String, host: String?, endpoint: String): String =
        "$host/$endpoint/$activationCode"

}
