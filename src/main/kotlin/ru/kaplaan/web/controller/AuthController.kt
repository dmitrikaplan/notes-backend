package ru.kaplaan.web.controller

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

@Validated
@Controller
@RequestMapping("/api/v1/auth/")
class AuthController (
    private val authService: AuthService,
    private val userMapper: UserMapper,
    private val userIdentificationMapper: UserIdentificationMapper
){

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("registration")
    fun registration(
        @RequestBody(required = true) @Validated(OnCreate::class)
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

    @PostMapping("login")
    fun login(
        @RequestBody(required = true) @Validated(OnCreate::class)
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
    fun activateAccount(
        @PathVariable("code", required = true) @Length(min = 1)
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
    fun passwordRecovery(
        @RequestBody(required = true) @Validated(OnCreate::class)
        userIdentificationDto: UserIdentificationDto
    ): ResponseEntity<MessageResponse> {
        return try {
            val userIdentification = userIdentificationMapper.toEntity(userIdentificationDto)
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
}
