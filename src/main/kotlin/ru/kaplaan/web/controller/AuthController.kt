package ru.kaplaan.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.mail.MessagingException
import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.domain.exception.user.*
import ru.kaplaan.dto.mapper.UserIdentificationMapper
import ru.kaplaan.dto.mapper.UserMapper
import ru.kaplaan.service.AuthService
import ru.kaplaan.web.dto.response.jwt.JwtResponse
import ru.kaplaan.web.dto.response.message.MessageResponse
import ru.kaplaan.web.dto.user.UserDto
import ru.kaplaan.web.dto.user.UserIdentificationDto
import ru.kaplaan.web.validation.OnCreate
import ru.kaplaan.web.validation.OnRecovery

@Validated
@Controller
@RequestMapping("/api/v1/auth/")
@Tag(name = "Auth Controller", description = "Контроллер аутентификации")
class AuthController (
    private val authService: AuthService,
    private val userMapper: UserMapper,
    private val userIdentificationMapper: UserIdentificationMapper
){

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("registration")
    @Operation(
        summary = "Регистрация пользователя",
    )
    fun registration(
        @RequestBody(required = true) @Validated(OnCreate::class)
        @Parameter(description = "логин, почта и пароль пользователя в формате json", required = true)
        userDto: UserDto
    ): ResponseEntity<MessageResponse> {
        return try {
            authService.registration(userMapper.toEntity(userDto))
            val messageResponse = MessageResponse("Код подтверждения отправлен вам на почту")
            log.info("Код подтверждения для пользователя ${userDto.getLogin()!!.uppercase()} отправлен на почту")
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
    }

    @Operation(
        summary = "Авторизация пользователя"
    )
    @PostMapping("login")
    fun login(
        @RequestBody(required = true) @Validated(OnCreate::class)
        @Parameter(description = "логин или почта пользователя и пароль в формате json", required = true)
        userIdentificationDto: UserIdentificationDto
    ): ResponseEntity<JwtResponse> {
        return try {
            val userIdentification = userIdentificationMapper.toEntity(userIdentificationDto)
            val jwtResponse = authService.login(userIdentification)
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

    @GetMapping("activation/{code}")
    @Operation(
        summary = "Активация аккаунта пользователя"
    )
    fun activateAccount(
        @PathVariable("code", required = true) @Length(min = 1)
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

    @PostMapping("recovery")
    @Operation(
        summary = "Восстановление доступа пользователя"
    )
    fun passwordRecovery(
        @RequestBody(required = true) @Validated(OnRecovery::class)
        @Parameter(description = "логин или почта пользователя и пароль в формате json", required = true)
        userIdentificationDto: UserIdentificationDto
    ): ResponseEntity<MessageResponse> {
        return try {
            val userIdentification = userIdentificationMapper.toEntity(userIdentificationDto)
            authService.passwordRecovery(userIdentification.getLoginOrEmail())
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
}
