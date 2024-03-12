package ru.kaplaan.notes.web.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.domain.domain.web.response.MessageResponse
import ru.kaplaan.domain.domain.web.validation.OnCreate
import ru.kaplaan.domain.domain.web.validation.OnUpdate
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
        principal: Principal,
    ): ResponseEntity<NoteDto> =
        noteService.addNote(noteDto.toEntity(principal.name)).let { note ->
            ResponseEntity.status(HttpStatus.OK).body(note.toDto())
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
        principal: Principal,
    ): ResponseEntity<NoteDto> =
        noteService.updateNote(noteDto.toEntity(principal.name)).let { note ->
            ResponseEntity.status(HttpStatus.OK).body(note.toDto())
        }

    @DeleteMapping("/delete/{id}")
    @Operation(
        summary = "Удаление заметки",
        description = "Удаление заметки пользователя"
    )
    @SecurityRequirement(name = "JWT")
    fun deleteNote(
        @PathVariable @Min(0)
        @Parameter(description = "id заметки, которую надо удалить", required = true)
        id: Long,
    ): ResponseEntity<MessageResponse> {
        noteService.deleteNote(id)
        return MessageResponse("Заметка успешно удалена").let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }

    @GetMapping("/get/all")
    @Operation(
        summary = "Получение заметок",
        description = "Получение заметок пользователя с сервера"
    )
    @SecurityRequirement(name = "JWT")
    fun getNotes(principal: Principal): List<NoteDto> =
        noteService.getAllNotes(principal.name).toDto()
}
