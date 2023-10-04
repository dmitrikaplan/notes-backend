package ru.kaplaan.web.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class JsonResponse {
    fun toJson() =
        Json.encodeToString(this)
}