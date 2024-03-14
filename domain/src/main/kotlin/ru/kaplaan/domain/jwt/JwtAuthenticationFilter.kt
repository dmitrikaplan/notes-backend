package ru.kaplaan.domain.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.web.filter.OncePerRequestFilter


class JwtAuthenticationFilter(
    private val jwtAuthenticationConverter: AuthenticationConverter,
    private val authenticationManager: AuthenticationManager,
    private val authenticationEntryPoint: AuthenticationEntryPoint
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
        ?: run {
            filterChain.doFilter(request, response)
            return;
        }

        try{
            val authentication = authenticationManager.authenticate(authenticationRequest)
            val securityContext = securityContextHolderStrategy.createEmptyContext().apply {
                this.authentication = authentication
            }

            securityContextHolderStrategy.context = securityContext
            securityContextRepository.saveContext(securityContext, request, response)

        } catch (e: AuthenticationException){
            securityContextHolderStrategy.clearContext()
            response.sendError(HttpStatus.UNAUTHORIZED.value())
            authenticationEntryPoint.commence(request, response, e)
            return
        }

        filterChain.doFilter(request, response)

    }
}
