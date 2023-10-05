package ru.kaplaan.web.controller

import jakarta.validation.ConstraintViolationException
import jakarta.validation.constraints.Min
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.domain.exception.note.NoteCannotBeAddedException
import ru.kaplaan.domain.exception.note.NoteCannotUpdatedException
import ru.kaplaan.service.NoteService
import ru.kaplaan.web.dto.response.message.MessageResponse
import ru.kaplaan.web.dto.note.NoteDto
import ru.kaplaan.dto.mapper.NoteMapper
import ru.kaplaan.web.validation.OnCreate
import ru.kaplaan.web.validation.OnUpdate

@Validated
@Controller
@RequestMapping("/api/v1/notes")
class NoteController(
    private val noteService: NoteService,
    private val noteMapper: NoteMapper
) {
    private val log = LoggerFactory.getLogger(NoteController::class.java)
    @PostMapping("add")
    fun addNotes(
        @RequestBody(required = true) @Validated(OnCreate::class)
        noteDto: NoteDto
    ): ResponseEntity<NoteDto> {
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
    fun updateNote(
        @RequestBody(required = true) @Validated(OnUpdate::class)
        noteDto: NoteDto
    ): ResponseEntity<NoteDto> {
        return try {
            val note = noteMapper.toEntity(noteDto)
            val updatedNote = noteService.updateNote(note)
            ResponseEntity.status(HttpStatus.OK).body(noteMapper.toDto(updatedNote))
        }
        catch (e: NoteCannotUpdatedException) {
            log.debug(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }

    }

    @DeleteMapping("delete")
    fun deleteNote(
        @RequestBody(required = true) @Validated @Min(0)
        id: Long
    ): ResponseEntity<MessageResponse> {
        return try {
            noteService.deleteNote(id)
            val messageResponse = MessageResponse("Заметка успешно удалена")
            ResponseEntity.status(HttpStatus.OK).body(messageResponse)
        }
        catch (e: ConstraintViolationException){
            log.debug(e.message)
            val messageResponse = MessageResponse("id должен быть больше 0")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse)
        }
    }

    @GetMapping("get/all")
    @ResponseBody
    fun getNotes(): ResponseEntity<List<NoteDto>> {
        val notes = noteService.allNotes()
        val notesDto = noteMapper.toDto(notes)
        return ResponseEntity.status(HttpStatus.OK).body(notesDto)
    }
}
