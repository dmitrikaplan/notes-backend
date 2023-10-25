package ru.kaplaan.domain.entity.user

import org.springframework.security.core.GrantedAuthority

enum class Role: GrantedAuthority {
    ROLE_USER, ROLE_ADMIN;

    override fun getAuthority(): String {
        return this.name
    }
}