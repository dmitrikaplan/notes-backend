package ru.kaplaan.domain.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.kaplaan.domain.exception.user.UserNotFoundException
import ru.kaplaan.service.JwtService

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val header = request.getHeader("Authorization")

        if(header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = header.substring(7)
        val username = jwtService.extractUsernameFromAccessToken(jwtToken)

        if(SecurityContextHolder.getContext().authentication == null){
            try{
                val userDetails = userDetailsService.loadUserByUsername(username)
                val authenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                SecurityContextHolder.getContext().authentication = authenticationToken

            } catch (e: UserNotFoundException){
                log.info("Пользователь не найден")
            }
        }

        filterChain.doFilter(request, response)

    }
}
