package com.example.service

import com.example.utils.exceptions.NotValidUserException
import com.example.utils.model.entities.User
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class ValidationService {

    private val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    fun validateLogin(user: User) {
        if ((user.getLogin() == null || user.getLogin()!!.length <= 6 || user.getLogin()!!.length > 320) &&
            (user.getEmail() == null || !validateEmail(user.getEmail()!!) || user.getEmail()!!.length < 6 || user.getEmail()!!.length > 320) || user.getPassword() == null || user.getPassword()!!.length < 8
        ) throw NotValidUserException()
    }

    fun validateRegistration(user: User) {
        if ((user.getLogin() == null) || (user.getLogin()!!.length <= 6) || (user.getLogin()!!.length > 320) || (
                    user.getEmail() == null) || !validateEmail(user.getEmail()!!) || (
                    user.getPassword() == null) || (user.getPassword()!!.length < 8)
        ) throw NotValidUserException()
    }

    fun validateEmail(email: String): Boolean {

        val pattern = Pattern.compile(EMAIL_PATTERN)
        return pattern.matcher(email).matches()
    }
}
