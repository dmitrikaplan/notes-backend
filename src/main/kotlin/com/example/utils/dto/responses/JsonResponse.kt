package com.example.utils.dto.responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class JsonResponse {
    fun toJson() =
        Json.encodeToString(this)
}