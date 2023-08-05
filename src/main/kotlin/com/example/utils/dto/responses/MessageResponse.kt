package com.example.utils.dto.responses

import kotlinx.serialization.Serializable


@Serializable
data class MessageResponse(private val message: String?): JsonResponse()