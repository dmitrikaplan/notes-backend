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
import ru.kaplaan.domain.entity.User
import ru.kaplaan.domain.exception.*
import ru.kaplaan.service.AuthService
import ru.kaplaan.web.dto.MessageResponse
import ru.kaplaan.web.dto.UserWithoutLoginOrEmail

@Validated
@Controller
@RequestMapping("/api/v1/auth/")
class AuthController (
    private val authService: AuthService,
){

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("registration")
    fun registration(@RequestBody(required = true) @Valid user: User): ResponseEntity<String> {
        return try {
            authService.registration(user)
            val messageResponse = MessageResponse("Код подтверждения отправлен вам на почту")
            log.info("Код подтверждения для пользователя ${user.getLogin()!!.uppercase()} отправлен на почту")
            ResponseEntity.status(HttpStatus.OK).body(messageResponse.toJson())
        } 
        catch (e: UserAlreadyRegisteredException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageResponse.toJson())
        }
        catch (e: NotValidUserException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse.toJson())
        } 
        catch (e: MessagingException) {
            log.debug("Ошибка отправки сообщения на почту")
            val messageResponse = MessageResponse("Ошибка отправки сообщения на почту. Повторите попытку позже")
            ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(messageResponse.toJson())
        } 
        catch (e: UnexpectedActivationCodeException){
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse.toJson())
        } 
        catch (e: UnexpectedActivatedException){
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse.toJson())
        }
    }

    @PostMapping("login")
    fun login(
        @RequestBody(required = true) @Valid userWithoutLoginOrEmail: UserWithoutLoginOrEmail
    ): ResponseEntity<String> {
        return try {
            val jwtResponse = authService.login(userWithoutLoginOrEmail)
            ResponseEntity.status(HttpStatus.OK).body(jwtResponse.toJson())
        } 
        catch (e: UserNotFoundException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageResponse.toJson())
        } 
        catch (e: NotValidUserException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse.toJson())
        }
        catch (e: UnexpectedActivationCodeException){
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse.toJson())
        }
        catch (e: UnexpectedActivatedException){
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse.toJson())
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
    fun passwordRecovery(@RequestBody(required = true) @Valid userWithoutLoginOrEmail: UserWithoutLoginOrEmail): ResponseEntity<String> {
        return try {
            authService.passwordRecovery(userWithoutLoginOrEmail)
            val messageResponse = MessageResponse("Код восстановления отправлен Вам на почту")
            ResponseEntity.status(HttpStatus.OK).body(messageResponse.toJson())
        }
        catch (e: NotValidEmailException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse.toJson())
        }
        catch (e: UserNotFoundException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageResponse.toJson())
        }
        catch (e: MessagingException) {
            log.debug("Ошибка отправки сообщения на почту")
            val messageResponse = MessageResponse("Ошибка отправки сообщения на почту. Повторите попытку позже")
            ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body(messageResponse.toJson())
        }
    }
}
