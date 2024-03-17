package ru.kaplaan.notes.web.controller

import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.notes.service.NoteService
import ru.kaplaan.notes.web.mapper.toDto
import ru.kaplaan.notes.web.mapper.toEntity
import ru.kaplaan.notes.web.model.dto.NoteDto
import ru.kaplaan.notes.web.model.response.MessageResponse
import ru.kaplaan.notes.web.validation.OnCreate
import ru.kaplaan.notes.web.validation.OnUpdate
import java.security.Principal

@RestController
@RequestMapping("/note")
class NoteController(
    private val noteService: NoteService,
) {

    @PostMapping("/add/{username}")
    fun addNotes(
        @RequestBody @Validated(OnCreate::class)
        noteDto: NoteDto,
        @PathVariable username: String,
    ): ResponseEntity<NoteDto> {
        return noteService.addNote(noteDto.toEntity(username)).let { note ->
            ResponseEntity.status(HttpStatus.OK).body(note.toDto())
        }
    }

    @PostMapping("/update/{username}")
    fun updateNote(
        @RequestBody @Validated(OnUpdate::class)
        noteDto: NoteDto,
        @PathVariable username: String,
    ): ResponseEntity<NoteDto> =
        noteService.updateNote(noteDto.toEntity(username)).let { note ->
            ResponseEntity.status(HttpStatus.OK).body(note.toDto())
        }

    @DeleteMapping("/delete/{id}")
    fun deleteNote(
        @PathVariable @Min(0)
        id: Long,
    ): ResponseEntity<MessageResponse> {
        noteService.deleteNote(id)
        return MessageResponse("Заметка успешно удалена").let {
            ResponseEntity.status(HttpStatus.OK).body(it)
        }
    }

    @GetMapping("/get/all/{username}")
    fun getNotes(@PathVariable username: String): List<NoteDto> =
        noteService.getAllNotes(username).toDto()
}
