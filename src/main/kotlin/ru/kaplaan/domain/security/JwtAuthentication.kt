package ru.kaplaan.domain.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtAuthentication : Authentication {

    private var authenticated = false
    private var login: String? = null

    fun getLogin() =
        login

    fun setLogin(login: String){
        this.login = login
    }

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return null
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getDetails(): Any?{
        return null
    }

    override fun getPrincipal(): Any? {
        return null
    }

    override fun isAuthenticated(): Boolean {
        return authenticated
    }

    override fun setAuthenticated(authenticated: Boolean) {
        this.authenticated = authenticated
    }

    override fun getName(): String? {
        return null
    }
}
