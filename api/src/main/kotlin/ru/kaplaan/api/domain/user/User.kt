package ru.kaplaan.api.domain.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class User(): UserDetails{

    var id: Long? = null

    lateinit var email: String
    private lateinit var username: String

    private lateinit var password: String

    var activated: Boolean = false

    var activationCode: String? = null

    lateinit var role: Role

    constructor(
        email: String,
        username: String,
        password: String,
        role: Role
    ) :this() {
        this.email = email
        this.username = username
        this.password = password
        this.role = role
    }

    constructor(
        email: String,
        username: String,
        password: String,
        activated: Boolean,
        activationCode: String?,
        role: Role
    ): this(email, username, password, role){
        this.activated = activated
        this.activationCode = activationCode
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(role)
    }


    override fun getPassword(): String =
        password

    fun setPassword(password: String){
        this.password = password
    }

    override fun getUsername(): String =
        username

    fun setUsername(username: String){
        this.username = username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean =
        true

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return activated
    }
}