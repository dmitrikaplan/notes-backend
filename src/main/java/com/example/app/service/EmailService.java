package com.example.app.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void activateUserByEmail(String emailTo,String login, String activationCode) throws MessagingException;
    void recoveryPasswordByEmail(String emailTo, String login, String activationCode) throws  MessagingException;

}
