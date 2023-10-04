package ru.kaplaan.domain.entity

import jakarta.persistence.*
import jakarta.validation.constraints.AssertFalse
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import org.springframework.validation.annotation.Validated
import kotlin.math.max

@Entity
@Table(name = "user_table")
class User() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private var id: Long? = null
    @Email
    private var email: String? = null
    @Pattern(regexp = "^[a-zA-Z0-9]{6,320}$")
    private var login: String? = null
    @Length(min = 8, max = 1024)
    private var password: String? = null
    private var activated = false
    private var activationCode: String? = null

    constructor(
        email: String,
        login: String,
        password: String,
    ) : this() {
        this.email = email
        this.login = login
        this.password = password
    }

    fun getId() =
        id

    fun setId(id: Long) {
        this.id = id
    }

    fun getEmail() =
        email

    fun setEmail(email: String){
        this.email = email
    }

    fun getLogin() =
        login

    fun setLogin(login: String){
        this.login = login
    }

    fun getPassword() =
        password

    fun setPassword(password: String){
        this.password = password
    }

    fun getActivated() =
        activated

    fun setActivated(activated: Boolean){
        this.activated = activated
    }

    fun getActivationCode() =
        activationCode

    fun setActivationCode(activationCode: String?){
        this.activationCode = activationCode
    }

    override fun toString(): String {
        return "User(id = $id, email = $email, login = $login, password = $password)"
    }
}