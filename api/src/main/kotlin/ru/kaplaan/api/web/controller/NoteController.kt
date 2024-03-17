package ru.kaplaan.api.web.controller


import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.api.service.NoteService
import ru.kaplaan.api.web.dto.note.NoteDto
import ru.kaplaan.api.web.dto.response.MessageResponse
import ru.kaplaan.api.web.validation.OnCreate
import ru.kaplaan.api.web.validation.OnUpdate
import java.security.Principal

@RestController
@RequestMapping("/api/v1/note")
class NoteController(
    private val noteService: NoteService
) {

    @PostMapping("/add")
    fun addNote(
        @RequestBody @Validated(OnCreate::class)
        noteDto: NoteDto,
        principal: Principal,
    ): ResponseEntity<NoteDto> = noteService.addNote(noteDto, principal.name)

    @PostMapping("/update")
    fun updateNote(
        @RequestBody @Validated(OnUpdate::class)
        noteDto: NoteDto,
        principal: Principal,
    ): ResponseEntity<NoteDto> = noteService.updateNote(noteDto, principal.name)

    @DeleteMapping("/delete/{id}")
    fun deleteNote(
        @PathVariable @Min(0)
        id: Long,
    ): ResponseEntity<MessageResponse> = noteService.deleteNote(id)

    @GetMapping("/get/all")
    fun getNotes(principal: Principal): List<NoteDto> =
        noteService.getNotes(principal.name)
}
