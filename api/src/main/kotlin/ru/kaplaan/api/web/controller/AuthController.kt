package ru.kaplaan.api.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import ru.kaplaan.api.service.AuthService
import ru.kaplaan.api.web.dto.refresh_token.RefreshTokenDto
import ru.kaplaan.api.web.dto.response.JwtResponse
import ru.kaplaan.api.web.dto.response.MessageResponse
import ru.kaplaan.api.web.dto.user.UserDto
import ru.kaplaan.api.web.dto.user.UserIdentificationDto
import ru.kaplaan.api.web.validation.OnCreate
import ru.kaplaan.api.web.validation.OnRecovery

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Контроллер аутентификации")
class AuthController(
    private val authService: AuthService
) {

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/registration/user")
    @Operation(summary = "Регистрация пользователя")
    fun registerUser(
        @RequestBody @Validated(OnCreate::class)
        @Parameter(description = "логин, почта и пароль пользователя в формате json", required = true)
        userDto: Mono<UserDto>,
    ): Mono<ResponseEntity<MessageResponse>> = authService.registerUser(userDto)


    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/login")
    fun login(
        @RequestBody @Validated(OnCreate::class)
        @Parameter(description = "логин или почта пользователя и пароль в формате json", required = true)
        userIdentificationDto: Mono<UserIdentificationDto>,
    ): Mono<ResponseEntity<JwtResponse>> = authService.login(userIdentificationDto)

    @GetMapping("/activation/{code}")
    @Operation(
        summary = "Активация аккаунта пользователя"
    )
    fun activateAccount(
        @PathVariable @Length(min = 1)
        @Parameter(description = "код активации аккаунта", required = true)
        code: String,
    ): Mono<ResponseEntity<String>> = authService.activateAccount(code)

    @PostMapping("/recovery")
    @Operation(
        summary = "Восстановление доступа пользователя"
    )
    fun passwordRecovery(
        @RequestBody(required = true) @Validated(OnRecovery::class)
        @Parameter(description = "логин или почта пользователя и пароль в формате json", required = true)
        userIdentificationDto: Mono<UserIdentificationDto>,
    ): Mono<ResponseEntity<MessageResponse>> = authService.passwordRecovery(userIdentificationDto)

    @Operation(
        summary = "Обновление jwt access токена"
    )
    @PostMapping("/refresh")
    fun refresh(
        @Parameter(description = "Refresh token в формате json", required = true)
        @RequestBody refreshTokenDto: Mono<RefreshTokenDto>
    ): Mono<ResponseEntity<JwtResponse>> = authService.refresh(refreshTokenDto)
}
