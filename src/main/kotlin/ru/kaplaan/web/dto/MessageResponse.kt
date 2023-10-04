package ru.kaplaan.web.dto

import kotlinx.serialization.Serializable


@Serializable
data class MessageResponse(private val message: String?): JsonResponse()