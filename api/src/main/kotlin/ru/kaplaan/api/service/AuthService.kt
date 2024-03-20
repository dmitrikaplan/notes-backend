package ru.kaplaan.api.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.kaplaan.api.web.dto.refresh_token.RefreshTokenDto
import ru.kaplaan.api.web.dto.response.JwtResponse
import ru.kaplaan.api.web.dto.response.MessageResponse
import ru.kaplaan.api.web.dto.user.UserDto
import ru.kaplaan.api.web.dto.user.UserIdentificationDto

@Service
interface AuthService {

    fun activateAccount(code: String): Mono<ResponseEntity<String>>

    fun registerUser(userDto: Mono<UserDto>): Mono<ResponseEntity<MessageResponse>>

    fun login(userIdentificationDto: Mono<UserIdentificationDto>): Mono<ResponseEntity<JwtResponse>>

    fun refresh(refreshTokenDto: Mono<RefreshTokenDto>): Mono<ResponseEntity<JwtResponse>>

    fun passwordRecovery(userIdentificationDto: Mono<UserIdentificationDto>): Mono<ResponseEntity<MessageResponse>>
}