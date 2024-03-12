package ru.kaplaan.notes.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.ConstraintViolationException
import jakarta.validation.constraints.Min
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.domain.domain.web.response.MessageResponse
import ru.kaplaan.domain.domain.web.validation.OnCreate
import ru.kaplaan.domain.domain.web.validation.OnUpdate
import ru.kaplaan.notes.domain.exception.NoteCannotBeAddedException
import ru.kaplaan.notes.domain.exception.NoteCannotUpdatedException
import ru.kaplaan.notes.service.NoteService
import ru.kaplaan.notes.web.dto.NoteDto
import ru.kaplaan.notes.web.mapper.toDto
import ru.kaplaan.notes.web.mapper.toEntity
import java.security.Principal

@RestController
@RequestMapping("/api/v1/notes")
@Tag(name = "Note Controller", description = "Контроллер заметок")
class NoteController(
    private val noteService: NoteService,
) {
    private val log = LoggerFactory.getLogger(NoteController::class.java)
    @PostMapping("/add")
    @Operation(
        summary = "Добавление заметки",
        description = "Добавление заметки пользователя, который произвел вход в аккаунт, на сервер"
    )
    @SecurityRequirement(name = "JWT")
    fun addNotes(
        @RequestBody @Validated(OnCreate::class)
        @Parameter(description = "id, заголовок, текст и владелец заметки", required = true)
        noteDto: NoteDto,
        principal: Principal
    ): ResponseEntity<NoteDto> {
        return try {
            val returnedNote = noteService.addNote(noteDto.toEntity(principal.name))
            ResponseEntity.status(HttpStatus.OK).body(returnedNote.toDto())
        }
        catch (e: NoteCannotBeAddedException) {
            log.debug(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @PostMapping("/update")
    @Operation(
        summary = "Обновление заметки",
        description = "Обновление заметки пользователя"
    )
    @SecurityRequirement(name = "JWT")
    fun updateNote(
        @RequestBody @Validated(OnUpdate::class)
        @Parameter(description = "id, заголовок, текст и владелец заметки", required = true)
        noteDto: NoteDto,
        principal: Principal
    ): ResponseEntity<NoteDto> {
        return try {
            val note = noteDto.toEntity(principal.name)
            val updatedNote = noteService.updateNote(note)
            ResponseEntity.status(HttpStatus.OK).body(updatedNote.toDto())
        }
        catch (e: NoteCannotUpdatedException) {
            log.debug(e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }

    }

    @DeleteMapping("/delete")
    @Operation(
        summary = "Удаление заметки",
        description = "Удаление заметки пользователя"
    )
    @SecurityRequirement(name = "JWT")
    fun deleteNote(
        @RequestBody @Validated @Min(0)
        @Parameter(description = "id заметки, которую надо удалить", required = true)
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

    @GetMapping("/get/all")
    @Operation(
        summary = "Получение заметок",
        description = "Получение заметок пользователя с сервера"
    )
    @SecurityRequirement(name = "JWT")
    fun getNotes(): List<NoteDto> {
        val notes = noteService.allNotes()
        return notes.toDto()
    }
}
