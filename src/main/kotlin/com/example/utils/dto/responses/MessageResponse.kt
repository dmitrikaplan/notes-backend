package com.example.utils.dto.responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
data class MessageResponse(private val message: String?){

    fun toJson() =
        Json.encodeToString(this)
}