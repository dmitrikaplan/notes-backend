package com.example.service

import com.example.utils.model.JwtResponse
import com.example.utils.model.entities.User
import org.springframework.stereotype.Service

@Service
interface AuthService {
    fun login(user: User): JwtResponse

    fun registration(user: User)

    fun activateAccount(code: String)

    fun passwordRecovery(user: User)
}
