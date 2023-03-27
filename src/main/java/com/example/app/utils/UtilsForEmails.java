package com.example.app.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UtilsForEmails {


    public static String getMessageForActivation(String email, String host, String activationCode){
        return String.format(
                "Привет, %s \n" +
                        "Добро пожаловать в Kittynotes. Пожалуйста, для подтверждения почты перейдите" +
                        " по ссылке %s/activate/%s", email, host, activationCode
        );
    }

    public static String getSubjectForActivation(){
        return "Активация аккаунта";
    }
}
