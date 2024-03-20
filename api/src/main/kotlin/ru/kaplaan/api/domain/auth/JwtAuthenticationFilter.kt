package ru.kaplaan.api.domain.auth

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.body
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.util.context.Context
import ru.kaplaan.api.domain.exception.BadResponseException
import ru.kaplaan.api.domain.exception.JwtTokenNotFoundException
import ru.kaplaan.api.domain.exception.UserNotAuthenticatedException
import ru.kaplaan.api.web.dto.authentication.AuthenticationDto
import ru.kaplaan.api.web.mapper.toDto
import ru.kaplaan.api.web.mapper.toUsernamePasswordAuthentication


class JwtAuthenticationFilter(
    private val url: String,
    private val jwtAuthenticationConverter: ServerAuthenticationConverter,
    private val webClient: WebClient,
) : WebFilter {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
            return jwtAuthenticationConverter.convert(exchange)
                .switchIfEmpty{
                    Mono.error(JwtTokenNotFoundException())
                }
                .map {
                    it.toDto()
                }
                .authenticationRequest()
                .map {
                    ReactiveSecurityContextHolder.withAuthentication(it)
                }
                .flatMap { context ->
                    chain.filter(exchange).contextWrite(context)
                }
                .onErrorResume(AuthenticationException::class.java) {
                    chain.filter(exchange)
                }

    }


    private fun Mono<AuthenticationDto>.authenticationRequest(): Mono<UsernamePasswordAuthenticationToken> {
        return webClient
            .post()
            .uri(url)
            .body(this)
            .retrieve()
            .toEntity(AuthenticationDto::class.java)
            .handle { response, sink ->
                if (response.statusCode != HttpStatus.OK || response.body == null)
                    sink.error(UserNotAuthenticatedException())
                else sink.next(response.body!!.toUsernamePasswordAuthentication())
            }
    }
}
