package ru.kaplaan.notes.domain.exceptionHandler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.kaplaan.notes.domain.exception.NoteException


@ControllerAdvice
class NoteExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(NoteException::class)
    fun noteExceptionHandler(noteException: NoteException): ResponseEntity<ProblemDetail>{
        ProblemDetail
            .forStatusAndDetail(HttpStatus.BAD_REQUEST, noteException.message)
            .apply {
                setProperty("errors", noteException.message)
                log.debug(noteException.message)
                return ResponseEntity.badRequest().body(this)
            }
    }
}