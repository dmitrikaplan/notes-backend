package ru.kaplaan.domain.domain.user

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class User(): UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(unique = true)
    lateinit var email: String

    @Column(unique = true)
    private lateinit var username: String

    private lateinit var password: String

    var activated: Boolean = false

    var activationCode: String? = null

    @Enumerated(value = EnumType.STRING)
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


    override fun getAuthorities(): List<GrantedAuthority> {
        return listOf(role)
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