package ru.kaplaan.api.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.kaplaan.api.service.NoteService
import ru.kaplaan.api.web.dto.note.NoteDto
import ru.kaplaan.api.web.dto.response.MessageResponse

@Service
class NoteServiceImpl(
    private val webClient: WebClient
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


    override fun addNote(noteDto: NoteDto, username: String): Mono<ResponseEntity<NoteDto>> =
        webClient
            .post()
            .uri("$baseUrl$addEndpoint/$username")
            .body(BodyInserters.fromValue(noteDto))
            .retrieve()
            .toEntity(NoteDto::class.java)

    override fun updateNote(noteDto: NoteDto, username: String): Mono<ResponseEntity<NoteDto>> =
        webClient
            .post()
            .uri("$baseUrl$updateEndpoint/$username")
            .body(BodyInserters.fromValue(noteDto))
            .retrieve()
            .toEntity(NoteDto::class.java)

    override fun deleteNote(id: Long): Mono<ResponseEntity<MessageResponse>> =
        webClient
            .delete()
            .uri("$baseUrl$deleteEndpoint/$id")
            .retrieve()
            .toEntity(MessageResponse::class.java)


    override fun getNotes(username: String): Flux<NoteDto> =
        webClient
            .get()
            .uri("$baseUrl$getAllEndpoint/$username")
            .retrieve()
            .bodyToFlux(NoteDto::class.java)

}