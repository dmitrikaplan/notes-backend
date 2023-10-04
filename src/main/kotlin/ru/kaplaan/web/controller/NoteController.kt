package ru.kaplaan.web.controller

import jakarta.validation.ConstraintViolationException
import jakarta.validation.constraints.Min
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.domain.entity.Note
import ru.kaplaan.domain.exception.notes.NoteCannotBeAddedException
import ru.kaplaan.domain.exception.notes.NoteCannotUpdatedException
import ru.kaplaan.service.NoteService
import ru.kaplaan.web.dto.MessageResponse

@Validated
@Controller
@RequestMapping("/api/v1/notes")
class NoteController(
    private val noteService: NoteService
) {
    private val log = LoggerFactory.getLogger(NoteController::class.java)
    @PostMapping("add")
    fun addNotes(@RequestBody(required = true) note: Note): ResponseEntity<String> {
        return try {
            val note1 = noteService.addNote(note)
            ResponseEntity.status(HttpStatus.OK).body(note1.toJson())
        }
        catch (e: NoteCannotBeAddedException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse.toJson())
        }
    }

    @PostMapping("update")
    fun updateNote(@RequestBody(required = true) note: Note): ResponseEntity<String> {
        return try {
            val note1 = noteService.updateNote(note)
            ResponseEntity.status(HttpStatus.OK).body(note1.toJson())
        }
        catch (e: NoteCannotUpdatedException) {
            log.debug(e.message)
            val messageResponse = MessageResponse(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse.toJson())
        }

    }

    @DeleteMapping("delete")
    fun deleteNote(@RequestBody(required = true) @Min(0) id: Long): ResponseEntity<String> {
        return try {
            noteService.deleteNote(id)
            val messageResponse = MessageResponse("Заметка успешно удалена")
            ResponseEntity.status(HttpStatus.OK).body(messageResponse.toJson())
        }
        catch (e: ConstraintViolationException){
            log.debug(e.message)
            val messageResponse = MessageResponse("id должен быть больше 0")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse.toJson())
        }
    }

    @GetMapping("get-all")
    @ResponseBody
    fun getNotes(): ResponseEntity<List<Note>> {
        val notes = noteService.allNotes()
        return ResponseEntity.status(HttpStatus.OK).body(notes)
    }
}
