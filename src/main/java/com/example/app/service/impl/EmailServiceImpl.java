package com.example.app.service.impl;


import com.example.app.service.EmailService;
import com.example.app.utils.enums.KindsOfEmailMessages;
import com.example.app.utils.enums.KindsOfSubjects;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;
    private SpringTemplateEngine springTemplateEngine;
    @Value("${kittynotes.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String mail;



    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine springTemplateEngine) {
        this.mailSender = mailSender;
        this.springTemplateEngine = springTemplateEngine;

    }

    @Override
    public void activateUserByEmail(String emailTo, String login, String activationCode) throws MessagingException {
        String templateLocation = KindsOfEmailMessages.REGISTRATION_EMAIL.getPathOfTemplate();
        String subject = KindsOfSubjects.SUBJECT_FOR_REGISTRATION.getSubject();
        String endpoint = "activation";
        Context context = new Context();
        context.setVariable("login", login);
        context.setVariable("activationLink", generateActivationLink(activationCode, host, endpoint));
        context.setVariable("subject", subject);
        sendEmail(emailTo, login, context, subject, templateLocation);
    }

    @Override
    public void recoveryPasswordByEmail(String emailTo, String login, String activationCode) throws MessagingException {
        String templateLocation = KindsOfEmailMessages.RECOVERY_EMAIL.getPathOfTemplate();
        String subject = KindsOfSubjects.SUBJECT_FOR_PASSWORD_RECOVERY.getSubject();
        String endpoint = "recovery";
        Context context = new Context();
        context.setVariable("login", login);
        context.setVariable("activationLink", generateActivationLink(activationCode, host, endpoint));
        context.setVariable("subject", subject);
        sendEmail(emailTo, login, context, subject, templateLocation);
    }


    private void sendEmail(String emailTo,String login, Context context, String subject, String templateLocation) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mailMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );
        String emailContent = springTemplateEngine.process(templateLocation, context);
        mimeMessageHelper.setFrom(mail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setTo(emailTo);
        mimeMessageHelper.setText(emailContent, true);
        mailSender.send(mailMessage);
    }


    private String generateActivationLink(String activationCode, String host, String endpoint){
        return String.format("%s/%s/%s", host, endpoint, activationCode);
    }

}
