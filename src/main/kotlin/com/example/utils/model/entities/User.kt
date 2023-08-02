package com.example.utils.model.entities

import jakarta.persistence.*

@Entity
@Table(name = "user_table")
class User() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private var id: Long? = null
    private var email: String? = null
    private var login: String? = null
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
}