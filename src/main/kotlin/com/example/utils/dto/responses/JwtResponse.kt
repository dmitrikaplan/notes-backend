package com.example.utils.dto.responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class JwtResponse(
    private val accessToken: String,
    private val refreshToken: String,
){

    private val type = "Bearer"


    fun toJson() =
        Json.encodeToString(this)

}

