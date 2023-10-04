package ru.kaplaan.web.dto

import kotlinx.serialization.Serializable

@Serializable
data class JwtResponse(
    private val accessToken: String,
    private val refreshToken: String,
): JsonResponse()

    //private val type = "Bearer"


