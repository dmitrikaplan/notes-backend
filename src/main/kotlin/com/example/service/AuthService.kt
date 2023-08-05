package com.example.service

import com.example.utils.dto.UserWithoutLoginOrEmail
import com.example.utils.dto.responses.JwtResponse
import com.example.utils.dto.entities.User
import org.springframework.stereotype.Service

@Service
interface AuthService {

    fun registration(user: User)

    fun login(userWithoutLoginOrEmail: UserWithoutLoginOrEmail): JwtResponse

    fun activateAccount(code: String)

    fun passwordRecovery(userWithoutLoginOrEmail: UserWithoutLoginOrEmail)
}
