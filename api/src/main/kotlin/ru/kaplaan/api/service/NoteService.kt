package ru.kaplaan.api.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.kaplaan.api.web.dto.note.NoteDto
import ru.kaplaan.api.web.dto.response.MessageResponse

@Service
interface NoteService {

    fun addNote(noteDto: NoteDto, username: String ): Mono<ResponseEntity<NoteDto>>

    fun updateNote(noteDto: NoteDto, username: String): Mono<ResponseEntity<NoteDto>>

    fun deleteNote(id: Long): Mono<ResponseEntity<MessageResponse>>

    fun getNotes(username: String): Flux<NoteDto>


}