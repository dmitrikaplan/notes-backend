package ru.kaplaan.api.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import ru.kaplaan.api.domain.exception.EmptyBodyException
import ru.kaplaan.api.service.AuthService
import ru.kaplaan.api.web.dto.refresh_token.RefreshTokenDto
import ru.kaplaan.api.web.dto.response.JwtResponse
import ru.kaplaan.api.web.dto.response.MessageResponse
import ru.kaplaan.api.web.dto.user.UserDto
import ru.kaplaan.api.web.dto.user.UserIdentificationDto

@Service
class AuthServiceImpl(
    private val restClient: RestClient
): AuthService {

    @Value("\${auth-server.base-url}")
    lateinit var baseUrl: String

    @Value("\${auth-server.endpoint.registration}")
    lateinit var registrationEndpoint: String

    @Value("\${auth-server.endpoint.login}")
    lateinit var loginEndpoint: String

    @Value("\${auth-server.endpoint.activation}")
    lateinit var activationEndpoint: String

    @Value("\${auth-server.endpoint.recovery}")
    lateinit var recoveryEndpoint: String

    @Value("\${auth-server.endpoint.refresh}")
    lateinit var refreshEndpoint: String

    override fun registerUser(userDto: UserDto): ResponseEntity<MessageResponse> =
        restClient
            .post()
            .uri("$baseUrl/$registrationEndpoint")
            .body(userDto)
            .retrieve()
            .toEntity(MessageResponse::class.java)

    override fun login(userIdentificationDto: UserIdentificationDto): ResponseEntity<JwtResponse> =
        restClient
            .post()
            .uri("$baseUrl/$loginEndpoint")
            .body(userIdentificationDto)
            .retrieve()
            .toEntity(JwtResponse::class.java)

    override fun activateAccount(code: String): ResponseEntity<String> =
        restClient
            .get()
            .uri("$baseUrl/$activationEndpoint/$code")
            .retrieve()
            .toEntity(String::class.java)

    override fun passwordRecovery(userIdentificationDto: UserIdentificationDto): ResponseEntity<MessageResponse> =
        restClient
            .post()
            .uri("$baseUrl/$recoveryEndpoint")
            .body(userIdentificationDto)
            .retrieve()
            .toEntity(MessageResponse::class.java)

    override fun refresh(refreshTokenDto: RefreshTokenDto): JwtResponse =
        restClient
            .post()
            .uri("$baseUrl/$refreshEndpoint")
            .body(refreshTokenDto)
            .retrieve()
            .toEntity(JwtResponse::class.java)
            .body ?: throw EmptyBodyException()
}