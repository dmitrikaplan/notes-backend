package ru.kaplaan.web.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import ru.kaplaan.web.validation.OnCreate

class UserDto(){
    @Email(message = "Email must not be null", groups = [OnCreate::class])
    private var email: String? = null
    @Pattern(
        regexp = "^[a-zA-Z0-9]{6,320}$",
        message = "Login should fit the login pattern",
        groups = [OnCreate::class]
    )
    private var login: String? = null
    @Length(
        min = 8, max = 1024,
        message = "The password must be greater than 8, but less than 1024",
        groups = [OnCreate::class]
    )
    private var password: String? = null

    constructor(
        email: String,
        login: String,
        password: String,
    ) : this() {
        this.email = email
        this.login = login
        this.password = password
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

    override fun toString(): String {
        return "User(email = $email, login = $login, password = $password)"
    }
}