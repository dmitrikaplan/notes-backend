package ru.kaplaan.authserver.domain.advice

import jakarta.mail.MessagingException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.kaplaan.domain.domain.exception.UserException


@ControllerAdvice
class AuthControllerAdvice {

    private val log = LoggerFactory.getLogger(javaClass)


    @ExceptionHandler(UserException::class)
    fun userExceptionHandler(userException: UserException): ResponseEntity<ProblemDetail> {
        ProblemDetail
            .forStatusAndDetail(HttpStatus.BAD_REQUEST, userException.message)
            .apply {
                setProperty("errors", userException.message)
                log.debug(userException.message)
                return ResponseEntity.badRequest().body(this)
            }
    }


    @ExceptionHandler(MessagingException::class)
    fun messagingExceptionHandler(messagingException: MessagingException): ResponseEntity<ProblemDetail> {
        ProblemDetail
            .forStatusAndDetail(HttpStatus.BAD_REQUEST, "Ошибка отправки сообщения на почту. Повторите попытку позже")
            .apply {
                setProperty("errors", "Ошибка отправки сообщения на почту. Повторите попытку позже")
                log.debug("Ошибка отправки сообщения на почту")
                return ResponseEntity.badRequest().body(this)
            }

    }
}