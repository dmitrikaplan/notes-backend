package ru.kaplaan.api.domain.exceptionHandler

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import reactor.core.publisher.Mono
import ru.kaplaan.api.domain.exception.BadResponseException

@ControllerAdvice
class ResponseExceptionHandler {


    @ExceptionHandler(BadResponseException::class)
    fun emptyBodyExceptionHandler(e: BadResponseException): Mono<ResponseEntity<ResponseEntity<ProblemDetail>>> {
        return e.response.map {
            ResponseEntity.status(it.statusCode.value()).body(it)
        }
    }

}