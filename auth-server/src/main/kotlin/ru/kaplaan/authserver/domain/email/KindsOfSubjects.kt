package ru.kaplaan.authserver.domain.email

enum class KindsOfSubjects(val subject: String) {
    SUBJECT_FOR_REGISTRATION("Подтверждение регистрации"),
    SUBJECT_FOR_PASSWORD_RECOVERY("Восстановление пароля")
}
