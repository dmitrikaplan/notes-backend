package ru.kaplaan.api.domain.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.web.client.RestClient
import org.springframework.web.filter.OncePerRequestFilter
import ru.kaplaan.api.web.dto.authentication.AuthenticationDto
import ru.kaplaan.api.web.mapper.toDto
import ru.kaplaan.api.web.mapper.toUsernamePasswordAuthentication


class JwtAuthenticationFilter(
    private val jwtAuthenticationConverter: AuthenticationConverter,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val restClient: RestClient,
    private val url: String,

) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    private val securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()
    private val securityContextRepository = RequestAttributeSecurityContextRepository()



    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authenticationRequest = jwtAuthenticationConverter.convert(request)
        ?: run{
            filterChain.doFilter(request, response)
            return
        }

        try{
            val authentication = authenticationRequest(authenticationRequest.toDto())
            val securityContext = securityContextHolderStrategy.createEmptyContext().apply {
                this.authentication = authentication
            }

            securityContextHolderStrategy.context = securityContext
            securityContextRepository.saveContext(securityContext, request, response)

        } catch (e: AuthenticationException){
            securityContextHolderStrategy.clearContext()
            log.debug(e.message)
            authenticationEntryPoint.commence(request, response, e)
            return
        }

        filterChain.doFilter(request, response)

    }



    private fun authenticationRequest(authenticationDto: AuthenticationDto): Authentication{
            restClient
            .post()
            .uri(url)
            .body(authenticationDto)
            .retrieve()
            .toEntity(AuthenticationDto::class.java)
            .let { response ->
                if (response.statusCode.value() == HttpStatus.OK.value() && response.body != null)
                    return response.body!!.toUsernamePasswordAuthentication()
                else throw BadCredentialsException("Невозможно аутентифицировать пользователя!")
            }

    }
}
