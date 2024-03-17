package ru.kaplaan.api.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import ru.kaplaan.api.domain.exception.EmptyBodyException
import ru.kaplaan.api.service.NoteService
import ru.kaplaan.api.web.dto.note.NoteDto
import ru.kaplaan.api.web.dto.response.MessageResponse

@Service
class NoteServiceImpl(
    private val restClient: RestClient
): NoteService {

    @Value("\${note-server.base-url}")
    lateinit var baseUrl: String

    @Value("\${note-server.endpoint.add}")
    lateinit var addEndpoint: String

    @Value("\${note-server.endpoint.update}")
    lateinit var updateEndpoint: String

    @Value("\${note-server.endpoint.delete}")
    lateinit var deleteEndpoint: String

    @Value("\${note-server.endpoint.get-all}")
    lateinit var getAllEndpoint: String


    override fun addNote(noteDto: NoteDto, username: String): ResponseEntity<NoteDto> =
        restClient
            .post()
            .uri("$baseUrl$addEndpoint/$username")
            .body(noteDto)
            .retrieve()
            .toEntity(NoteDto::class.java)

    override fun updateNote(noteDto: NoteDto, username: String): ResponseEntity<NoteDto> =
        restClient
            .post()
            .uri("$baseUrl$updateEndpoint/$username")
            .body(noteDto)
            .retrieve()
            .toEntity(NoteDto::class.java)

    override fun deleteNote(id: Long): ResponseEntity<MessageResponse> =
        restClient
            .delete()
            .uri("$baseUrl$deleteEndpoint/$id")
            .retrieve()
            .toEntity(MessageResponse::class.java)


    override fun getNotes(username: String): List<NoteDto> =
        restClient
            .get()
            .uri("$baseUrl$getAllEndpoint/$username")
            .retrieve()
            .toEntity(List::class.java)
            .body?.let {
            it as List<NoteDto>
        } ?: throw EmptyBodyException()

}