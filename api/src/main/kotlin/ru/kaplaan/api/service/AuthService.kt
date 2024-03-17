package ru.kaplaan.api.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.kaplaan.api.web.dto.refresh_token.RefreshTokenDto
import ru.kaplaan.api.web.dto.response.JwtResponse
import ru.kaplaan.api.web.dto.response.MessageResponse
import ru.kaplaan.api.web.dto.user.UserDto
import ru.kaplaan.api.web.dto.user.UserIdentificationDto

@Service
interface AuthService {

    fun activateAccount(code: String): ResponseEntity<String>

    fun registerUser(userDto: UserDto): ResponseEntity<MessageResponse>

    fun login(userIdentificationDto: UserIdentificationDto): ResponseEntity<JwtResponse>

    fun refresh(refreshTokenDto: RefreshTokenDto): JwtResponse

    fun passwordRecovery(userIdentificationDto: UserIdentificationDto): ResponseEntity<MessageResponse>
}