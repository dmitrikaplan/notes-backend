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
class User(){

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    lateinit var email: String
    lateinit var login: String
    lateinit var password: String

    var activated: Boolean? = false

    var activationCode: String? = null

    constructor(
        id: Long,
        email: String,
        login: String,
        password: String,
        activated: Boolean,
        activationCode: String?
    ): this(){
        this.id = id
        this.email = email
        this.login = login
        this.password = password
        this.activated = activated
        this.activationCode = activationCode
    }

    constructor(
        email: String,
        login: String,
        password: String,
        activated: Boolean,
        activationCode: String?
    ): this(){
        this.email = email
        this.login = login
        this.password = password
        this.activated = activated
        this.activationCode = activationCode
    }
}