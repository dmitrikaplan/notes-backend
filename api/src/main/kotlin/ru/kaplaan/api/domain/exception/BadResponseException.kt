package ru.kaplaan.api.domain.exception

import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono

class BadResponseException(val response: Mono<ResponseEntity<ProblemDetail>>): RuntimeException()