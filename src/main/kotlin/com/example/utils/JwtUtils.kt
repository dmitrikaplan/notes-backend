package com.example.utils

import com.example.utils.security.JwtAuthentication
import io.jsonwebtoken.Claims

object JwtUtils {
    fun generate(claims: Claims): JwtAuthentication {
        val jwtInfoToken = JwtAuthentication()
        jwtInfoToken.setLogin(claims.subject)
        return jwtInfoToken
    }
}
