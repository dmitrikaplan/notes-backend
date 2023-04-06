package com.example.app.utils.enums;

public enum KindsOfSubjects {
    SUBJECT_FOR_REGISTRATION("Подтверждение регистрации"),
    SUBJECT_FOR_PASSWORD_RECOVERY("Восстановление пароля");
    private final String subject;
    KindsOfSubjects(String subject){
        this.subject = subject;
    }

    public String getSubject(){
        return subject;
    }
}
