package ru.kaplaan.api.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.body
import reactor.core.publisher.Mono
import ru.kaplaan.api.service.AuthService
import ru.kaplaan.api.web.dto.refresh_token.RefreshTokenDto
import ru.kaplaan.api.web.dto.response.JwtResponse
import ru.kaplaan.api.web.dto.response.MessageResponse
import ru.kaplaan.api.web.dto.user.UserDto
import ru.kaplaan.api.web.dto.user.UserIdentificationDto

@Service
class AuthServiceImpl(
    private val webClient: WebClient
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

    override fun registerUser(userDto: Mono<UserDto>): Mono<ResponseEntity<MessageResponse>> =
        webClient
            .post()
            .uri("$baseUrl/$registrationEndpoint")
            .body(userDto)
            .retrieve()
            .toEntity(MessageResponse::class.java)


    override fun login(userIdentificationDto: Mono<UserIdentificationDto>): Mono<ResponseEntity<JwtResponse>> =
        webClient
            .post()
            .uri("$baseUrl/$loginEndpoint")
            .body(userIdentificationDto)
            .retrieve()
            .toEntity(JwtResponse::class.java)

    override fun activateAccount(code: String): Mono<ResponseEntity<String>> =
        webClient
            .get()
            .uri("$baseUrl/$activationEndpoint/$code")
            .retrieve()
            .toEntity(String::class.java)

    override fun passwordRecovery(userIdentificationDto: Mono<UserIdentificationDto>): Mono<ResponseEntity<MessageResponse>> =
        webClient
            .post()
            .uri("$baseUrl/$recoveryEndpoint")
            .body(userIdentificationDto)
            .retrieve()
            .toEntity(MessageResponse::class.java)

    override fun refresh(refreshTokenDto: Mono<RefreshTokenDto>): Mono<ResponseEntity<JwtResponse>> =
        webClient
            .post()
            .uri("$baseUrl/$refreshEndpoint")
            .body(refreshTokenDto)
            .retrieve()
            .toEntity(JwtResponse::class.java)
}