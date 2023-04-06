package com.example.app.utils.enums;

public enum KindsOfEmailMessages {
    REGISTRATION_EMAIL("registration"),
    RECOVERY_EMAIL("recovery");
    private final String pathOfTemplate;
    KindsOfEmailMessages(String pathOfTemplate) {
        this.pathOfTemplate = pathOfTemplate;
    }

    public String getPathOfTemplate() {
        return pathOfTemplate;
    }
}
