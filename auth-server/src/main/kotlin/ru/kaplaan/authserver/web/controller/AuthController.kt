package ru.kaplaan.authserver.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.authserver.service.AuthService
import ru.kaplaan.authserver.web.dto.refresh_token.RefreshTokenDto
import ru.kaplaan.authserver.web.dto.response.jwt.JwtResponse
import ru.kaplaan.authserver.web.dto.user.UserDto
import ru.kaplaan.authserver.web.dto.user.UserIdentificationDto
import ru.kaplaan.authserver.web.mapper.toEntity
import ru.kaplaan.domain.domain.user.Role
import ru.kaplaan.domain.domain.web.response.MessageResponse
import ru.kaplaan.domain.domain.web.validation.OnCreate
import ru.kaplaan.domain.domain.web.validation.OnRecovery
import java.security.Principal


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Контроллер аутентификации")
class AuthController(
    private val authService: AuthService,
) {

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/registration/user")
    @Operation(summary = "Регистрация пользователя")
    fun registerUser(
        @RequestBody @Validated(OnCreate::class)
        @Parameter(description = "логин, почта и пароль пользователя в формате json", required = true)
        userDto: UserDto,
    ): ResponseEntity<MessageResponse> {
        authService.register(userDto.toEntity(Role.ROLE_USER))
        log.info("Код подтверждения для пользователя ${userDto.username.uppercase()} отправлен на почту")

        return MessageResponse("Код подтверждения отправлен вам на почту").let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }

    }


    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    fun login(
        @RequestBody @Validated(OnCreate::class)
        @Parameter(description = "логин или почта пользователя и пароль в формате json", required = true)
        userIdentificationDto: UserIdentificationDto,
    ): ResponseEntity<JwtResponse> =
        authService.authenticate(userIdentificationDto.toEntity()).let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }

    @GetMapping("/activation/{code}")
    @Operation(
        summary = "Активация аккаунта пользователя"
    )
    fun activateAccount(
        @PathVariable @Length(min = 1)
        @Parameter(description = "код активации аккаунта", required = true)
        code: String,
    ): ResponseEntity<String> =
        authService.activateAccount(code).let {
            ResponseEntity.status(HttpStatus.OK).body("Аккаунт успешно активирован")
        }

    @PostMapping("/recovery")
    @Operation(
        summary = "Восстановление доступа пользователя"
    )
    fun passwordRecovery(
        @RequestBody(required = true) @Validated(OnRecovery::class)
        @Parameter(description = "логин или почта пользователя и пароль в формате json", required = true)
        userIdentificationDto: UserIdentificationDto,
    ): ResponseEntity<MessageResponse> {
        authService.passwordRecovery(userIdentificationDto.toEntity())

        return MessageResponse("Код восстановления отправлен Вам на почту").let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }


    }

    @Operation(
        summary = "Обновление jwt access токена"
    )
    @PostMapping("/refresh")
    fun getNewRefreshToken(
        @Parameter(description = "Refresh token в формате json", required = true)
        @RequestBody refreshTokenDto: RefreshTokenDto
    ): JwtResponse =
         authService.refresh(refreshTokenDto.refreshToken)
}
