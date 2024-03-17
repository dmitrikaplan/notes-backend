package ru.kaplaan.api.domain.exceptionHandler

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestClientResponseException
import ru.kaplaan.api.domain.exception.EmptyBodyException

@ControllerAdvice
class ResponseExceptionHandler {



    @ExceptionHandler(HttpStatusCodeException::class)
    fun httpStatusCodeExceptionHandler(e: HttpStatusCodeException): ResponseEntity<ProblemDetail> {
        val message = e.message?.let {
            parserJson(e.message!!, ProblemDetail::class.java)
        } ?: ""

        ProblemDetail
            .forStatusAndDetail(HttpStatus.valueOf(e.statusCode.value()), message)
            .apply {
                setProperty("errors", message)
                return ResponseEntity.status(e.statusCode).body(this)
            }

    }


    @ExceptionHandler(RestClientResponseException::class)
    fun httpStatusCodeExceptionHandler(e: RestClientResponseException): ResponseEntity<ProblemDetail> {
        val message = e.message?.let {
            parserJson(e.message!!, ProblemDetail::class.java)
        } ?: ""

        ProblemDetail
            .forStatusAndDetail(HttpStatus.valueOf(e.statusCode.value()), message)
            .apply {
                setProperty("errors", message)
                return ResponseEntity.badRequest().body(this)
            }

    }


    @ExceptionHandler(EmptyBodyException::class)
    fun emptyBodyExceptionHandler(e: EmptyBodyException): ResponseEntity<ProblemDetail> =
        ProblemDetail
            .forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
            .apply {
                setProperty("errors", e.message)
            }
            .let {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(it)
            }


    private fun <T> parserJson(message: String, clazz: Class<T>): String{
        return try{
            ObjectMapper().readValue(message, ProblemDetail::class.java).detail
                ?: message
        } catch (e: JsonProcessingException){
            message
        } catch (e: JsonMappingException){
            message
        }


    }

}