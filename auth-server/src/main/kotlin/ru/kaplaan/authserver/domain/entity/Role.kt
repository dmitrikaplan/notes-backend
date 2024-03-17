package ru.kaplaan.authserver.domain.entity

import org.springframework.security.core.GrantedAuthority

enum class Role: GrantedAuthority{
    ROLE_USER, ROLE_ADMIN;

    override fun getAuthority(): String {
        return this.name
    }
}