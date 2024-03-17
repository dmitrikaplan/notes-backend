package ru.kaplaan.authserver.web.controller

import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.authserver.domain.entity.Role
import ru.kaplaan.authserver.service.AuthService
import ru.kaplaan.authserver.web.dto.authentication.AuthenticationDto
import ru.kaplaan.authserver.web.dto.refresh_token.RefreshTokenDto
import ru.kaplaan.authserver.web.dto.response.JwtResponse
import ru.kaplaan.authserver.web.dto.response.MessageResponse
import ru.kaplaan.authserver.web.dto.user.UserDto
import ru.kaplaan.authserver.web.dto.user.UserIdentificationDto
import ru.kaplaan.authserver.web.mapper.toDto
import ru.kaplaan.authserver.web.mapper.toEntity
import ru.kaplaan.authserver.web.mapper.toUnauthenticatedToken
import ru.kaplaan.authserver.web.validation.OnCreate
import ru.kaplaan.authserver.web.validation.OnRecovery


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/registration/user")
    fun registerUser(
        @RequestBody @Validated(OnCreate::class)
        userDto: UserDto,
    ): ResponseEntity<MessageResponse> {
        authService.register(userDto.toEntity(Role.ROLE_USER))
        log.info("Код подтверждения для пользователя ${userDto.username.uppercase()} отправлен на почту")

        return MessageResponse("Код подтверждения отправлен вам на почту").let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }

    }


    @PostMapping("/login")
    fun login(
        @RequestBody @Validated(OnCreate::class)
        userIdentificationDto: UserIdentificationDto,
    ): ResponseEntity<JwtResponse> =
        authService.authenticate(userIdentificationDto.toEntity()).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }

    @GetMapping("/activation/{code}")
    fun activateAccount(
        @PathVariable @Length(min = 1)
        code: String,
    ): ResponseEntity<String> =
        authService.activateAccount(code).let {
            ResponseEntity.status(HttpStatus.OK).body("Аккаунт успешно активирован")
        }

    @PostMapping("/recovery")
    fun passwordRecovery(
        @RequestBody(required = true) @Validated(OnRecovery::class)
        userIdentificationDto: UserIdentificationDto,
    ): ResponseEntity<MessageResponse> {
        authService.passwordRecovery(userIdentificationDto.toEntity())

        return MessageResponse("Код восстановления отправлен Вам на почту").let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }


    }

    @PostMapping("/authenticate")
    fun authenticate(
        @RequestBody authenticationDto: AuthenticationDto
    ): ResponseEntity<AuthenticationDto> =
        authService.authenticate(authenticationDto.toUnauthenticatedToken()).let {
            ResponseEntity.ok().body(it.toDto())
        }


    @PostMapping("/refresh")
    fun getNewRefreshToken(
        @RequestBody refreshTokenDto: RefreshTokenDto
    ): JwtResponse =
         authService.refresh(refreshTokenDto.refreshToken)
}
