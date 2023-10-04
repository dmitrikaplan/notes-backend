package ru.kaplaan.web.controller

import jakarta.validation.ConstraintViolationException
import jakarta.validation.constraints.Min
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.domain.exception.notes.NoteCannotBeAddedException
import ru.kaplaan.domain.exception.notes.NoteCannotUpdatedException
import ru.kaplaan.service.NoteService
import ru.kaplaan.web.dto.MessageDto
import ru.kaplaan.web.dto.NoteDto
import ru.kaplaan.dto.mapper.NoteMapper

@Validated
@Controller
@RequestMapping("/api/v1/notes")
class NoteController(
    private val noteService: NoteService,
    private val noteMapper: NoteMapper
) {
    private val log = LoggerFactory.getLogger(NoteController::class.java)
    @PostMapping("add")
    fun addNotes(@RequestBody(required = true) noteDto: NoteDto): ResponseEntity<NoteDto> {
        return try {
            val returnedNote = noteService.addNote(noteMapper.toEntity(noteDto))
            ResponseEntity.status(HttpStatus.OK).body(noteMapper.toDto(returnedNote))
        }
        catch (e: NoteCannotBeAddedException) {
            log.debug(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @PostMapping("update")
    fun updateNote(@RequestBody(required = true) noteDto: NoteDto): ResponseEntity<NoteDto> {
        return try {
            val updatedNote = noteService.updateNote(noteMapper.toEntity(noteDto))
            ResponseEntity.status(HttpStatus.OK).body(noteMapper.toDto(updatedNote))
        }
        catch (e: NoteCannotUpdatedException) {
            log.debug(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }

    }

    @DeleteMapping("delete")
    fun deleteNote(@RequestBody(required = true) @Validated @Min(0) id: Long): ResponseEntity<MessageDto> {
        return try {
            noteService.deleteNote(id)
            val messageDto = MessageDto("Заметка успешно удалена")
            ResponseEntity.status(HttpStatus.OK).body(messageDto)
        }
        catch (e: ConstraintViolationException){
            log.debug(e.message)
            val messageDto = MessageDto("id должен быть больше 0")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto)
        }
    }

    @GetMapping("get-all")
    @ResponseBody
    fun getNotes(): ResponseEntity<List<NoteDto>> {
        val notes = noteService.allNotes()
        return ResponseEntity.status(HttpStatus.OK).body(noteMapper.toDto(notes))
    }
}
