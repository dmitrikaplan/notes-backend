package ru.kaplaan.api.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.kaplaan.api.web.dto.note.NoteDto
import ru.kaplaan.api.web.dto.response.MessageResponse

@Service
interface NoteService {

    fun addNote(noteDto: NoteDto, username: String ): ResponseEntity<NoteDto>

    fun updateNote(noteDto: NoteDto, username: String): ResponseEntity<NoteDto>

    fun deleteNote(id: Long): ResponseEntity<MessageResponse>

    fun getNotes(username: String): List<NoteDto>


}