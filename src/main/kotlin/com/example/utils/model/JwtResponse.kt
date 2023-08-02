package com.example.utils.model

data class JwtResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null
) {
    private val type = "Bearer"
}
