package com.example.controller

import com.example.service.AuthService
import com.example.utils.exceptions.*
import com.example.utils.model.JwtResponse
import com.example.utils.model.entities.User
import jakarta.mail.MessagingException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth/")
class AuthController (
    private val authService: AuthService
){

    @PostMapping("registration")
    @ResponseBody // TODO: 7/24/23 добавить валидационный слой,
    //  который будет проверять отсутсвие activation и activationCode
    fun registration(@RequestBody(required = true) user: User): ResponseEntity<String> {
        return try {
            authService.registration(user)
            ResponseEntity.status(HttpStatus.OK).body("Код подтверждения отправлен вам на почту")
        } catch (e: UserAlreadyRegisteredException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        } catch (e: NotValidUserException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Некорректные(й) логин и/или пароль, и/или почта")
        } catch (e: MessagingException) {
            ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body("Ошибка отправки сообщения на почту. Повторите попытку позже")
        }
    }

    @PostMapping("login")
    fun login(
        @RequestBody(required = true) user: User
    ): ResponseEntity<JwtResponse> {
        return try {
            val jwtResponse = authService.login(user)
            ResponseEntity.status(HttpStatus.OK).body(jwtResponse)
        } catch (e: UserNotFoundException) {
            val jwtResponse = JwtResponse(e.message, null)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jwtResponse)
        } catch (e: NotValidUserException) {
            val jwtResponse = JwtResponse(e.message, null)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtResponse)
        }
    }

    @GetMapping("activation/{code}")
    fun activateAccount(@PathVariable("code", required = true) code: String): String {
        return try {
            authService.activateAccount(code)
            "Аккаунт успешно активирован"
        } catch (e: NotFoundUserByActivationCode) {
            "Аккаунт уже активирован"
        }
    }

    @PostMapping("recovery")
    fun passwordRecovery(@RequestBody(required = true) user: User): ResponseEntity<String> {
        return try {
            authService.passwordRecovery(user)
            ResponseEntity.status(HttpStatus.OK).body("Код восстановления отправлен Вам на почту")
        } catch (e: NotValidEmailException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный формат почты")
        } catch (e: UserNotFoundException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь с такой почтой не найден")
        } catch (e: MessagingException) {
            ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body("Ошибка отправки сообщения на почту. Повторите попытку позже")
        }
    }
}
