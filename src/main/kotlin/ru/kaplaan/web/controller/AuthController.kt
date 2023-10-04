package ru.kaplaan.web.controller

import jakarta.mail.MessagingException
import jakarta.validation.Valid
import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.domain.exception.*
import ru.kaplaan.service.AuthService
import ru.kaplaan.web.dto.JwtResponse
import ru.kaplaan.web.dto.MessageDto
import ru.kaplaan.web.dto.UserDto
import ru.kaplaan.web.dto.UserWithoutLoginOrEmail
import ru.kaplaan.web.validation.OnCreate
import ru.kaplaan.dto.mapper.UserMapper

@Validated
@Controller
@RequestMapping("/api/v1/auth/")
class AuthController (
    private val authService: AuthService,
    private val userMapper: UserMapper
){

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("registration")
    fun registration(
        @RequestBody(required = true) @Validated(OnCreate::class)
        userDto: UserDto
    ): ResponseEntity<MessageDto> {
        return try {
            authService.registration(userMapper.toEntity(userDto))
            val messageDto = MessageDto("Код подтверждения отправлен вам на почту")
            log.info("Код подтверждения для пользователя ${userDto.getLogin()!!.uppercase()} отправлен на почту")
            ResponseEntity.status(HttpStatus.OK).body(messageDto)
        } 
        catch (e: UserAlreadyRegisteredException) {
            log.debug(e.message)
            val messageDto = MessageDto(e.message)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageDto)
        }
        catch (e: NotValidUserException) {
            log.debug(e.message)
            val messageDto = MessageDto(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto)
        } 
        catch (e: MessagingException) {
            log.debug("Ошибка отправки сообщения на почту")
            val messageDto = MessageDto("Ошибка отправки сообщения на почту. Повторите попытку позже")
            ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(messageDto)
        } 
        catch (e: UnexpectedActivationCodeException){
            log.debug(e.message)
            val messageDto = MessageDto(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto)
        } 
        catch (e: UnexpectedActivatedException){
            log.debug(e.message)
            val messageDto = MessageDto(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto)
        }
    }

    @PostMapping("login")
    fun login(
        @RequestBody(required = true) @Valid userWithoutLoginOrEmail: UserWithoutLoginOrEmail
    ): ResponseEntity<JwtResponse> {
        return try {
            val jwtResponse = authService.login(userWithoutLoginOrEmail)
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
    fun activateAccount(@PathVariable("code", required = true) @Length(min = 1) code: String): ResponseEntity<String>{
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
    fun passwordRecovery(@RequestBody(required = true) @Valid userWithoutLoginOrEmail: UserWithoutLoginOrEmail): ResponseEntity<MessageDto> {
        return try {
            authService.passwordRecovery(userWithoutLoginOrEmail)
            val messageDto = MessageDto("Код восстановления отправлен Вам на почту")
            ResponseEntity.status(HttpStatus.OK).body(messageDto)
        }
        catch (e: NotValidEmailException) {
            log.debug(e.message)
            val messageDto = MessageDto(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto)
        }
        catch (e: UserNotFoundException) {
            log.debug(e.message)
            val messageDto = MessageDto(e.message)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageDto)
        }
        catch (e: MessagingException) {
            log.debug("Ошибка отправки сообщения на почту")
            val messageDto = MessageDto("Ошибка отправки сообщения на почту. Повторите попытку позже")
            ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(messageDto)
        }
    }
}
