package ru.kaplaan.domain.enums

enum class KindsOfEmailMessages(val pathOfTemplate: String) {
    REGISTRATION_EMAIL("registration"),
    RECOVERY_EMAIL("recovery")
}
