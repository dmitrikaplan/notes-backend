package ru.kaplaan.authserver.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.mail.MessagingException
import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.authserver.domain.exception.*
import ru.kaplaan.authserver.service.AuthService
import ru.kaplaan.authserver.web.dto.response.jwt.JwtResponse
import ru.kaplaan.authserver.web.dto.user.UserDto
import ru.kaplaan.authserver.web.dto.user.UserIdentificationDto
import ru.kaplaan.authserver.web.mapper.toEntity
import ru.kaplaan.domain.domain.exception.UserAlreadyRegisteredException
import ru.kaplaan.domain.domain.exception.UserNotFoundException
import ru.kaplaan.domain.domain.user.Role
import ru.kaplaan.domain.domain.web.response.MessageResponse
import ru.kaplaan.domain.domain.web.validation.OnCreate
import ru.kaplaan.domain.domain.web.validation.OnRecovery



@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Контроллер аутентификации")
class AuthController(
    private val authService: AuthService
){

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/registration/user")
    @Operation(
        summary = "Регистрация пользователя",
    )
    fun registration(
        @RequestBody @Validated(OnCreate::class)
        @Parameter(description = "логин, почта и пароль пользователя в формате json", required = true)
        userDto: UserDto
    ): ResponseEntity<MessageResponse> {
        return try {
            authService.registerUser(userDto.toEntity(Role.ROLE_USER))
            val messageResponse = MessageResponse("Код подтверждения отправлен вам на почту")
            log.info("Код подтверждения для пользователя ${userDto.username.uppercase()} отправлен на почту")
            ResponseEntity.status(HttpStatus.OK).body(messageResponse)
        } 
        catch (e: UserAlreadyRegisteredException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageResponse)
        }
        catch (e: NotValidUserException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse)
        } 
        catch (e: MessagingException) {
            log.debug("Ошибка отправки сообщения на почту")
            val messageResponse = MessageResponse("Ошибка отправки сообщения на почту. Повторите попытку позже")
            ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(messageResponse)
        } 
        catch (e: UnexpectedActivationCodeException){
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse)
        } 
        catch (e: UnexpectedActivatedException){
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse)
        }
        catch (e: RoleNotFoundException){
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse)
        }
    }

    @Operation(
        summary = "Авторизация пользователя"
    )
    @PostMapping("/login")
    fun login(
        @RequestBody @Validated(OnCreate::class)
        @Parameter(description = "логин или почта пользователя и пароль в формате json", required = true)
        userIdentificationDto: UserIdentificationDto
    ): ResponseEntity<JwtResponse> {
        return try {
            val userIdentification = userIdentificationDto.toEntity()
            val jwtResponse = authService.authenticate(userIdentification)
            ResponseEntity.status(HttpStatus.OK).body(jwtResponse)
        } 
        catch (e: UserNotFoundException) {
            log.debug(e.message)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        } 
        catch (e: NotValidUserException) {
            log.debug(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
        catch (e: UnexpectedActivationCodeException){
            log.debug(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
        catch (e: UnexpectedActivatedException){
            log.debug(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @GetMapping("/activation/{code}")
    @Operation(
        summary = "Активация аккаунта пользователя"
    )
    fun activateAccount(
        @PathVariable @Length(min = 1)
        @Parameter(description = "код активации аккаунта", required = true)
        code: String
    ): ResponseEntity<String>{
        return try {
            authService.activateAccount(code)
            ResponseEntity.status(HttpStatus.OK).body("Аккаунт успешно активирован")
        }
        catch (e: NotFoundUserByActivationCode) {
            log.debug("Аккаунт уже активирован")
            ResponseEntity.status(HttpStatus.OK).body("Аккаунт уже активирован")
        }
    }

    @PostMapping("/recovery")
    @Operation(
        summary = "Восстановление доступа пользователя"
    )
    fun passwordRecovery(
        @RequestBody(required = true) @Validated(OnRecovery::class)
        @Parameter(description = "логин или почта пользователя и пароль в формате json", required = true)
        userIdentificationDto: UserIdentificationDto
    ): ResponseEntity<MessageResponse> {
        return try {
            val userIdentification = userIdentificationDto.toEntity()
            authService.passwordRecovery(userIdentification)
            val messageResponse = MessageResponse("Код восстановления отправлен Вам на почту")
            ResponseEntity.status(HttpStatus.OK).body(messageResponse)
        }
        catch (e: NotValidEmailException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse)
        }
        catch (e: UserNotFoundException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageResponse)
        }
        catch (e: MessagingException) {
            log.debug("Ошибка отправки сообщения на почту")
            val messageResponse = MessageResponse("Ошибка отправки сообщения на почту. Повторите попытку позже")
            ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(messageResponse)
        }
    }


    @PostMapping("/refresh")
    fun getNewRefreshToken(): JwtResponse {
        // TODO: Добавить обновление refresh token
        return JwtResponse("", "")
    }
}
