package ru.kaplaan.domain.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import ru.kaplaan.domain.jwt.JwtProvider
import ru.kaplaan.domain.jwt.JwtUtils

@Component
class JwtFilter : GenericFilterBean() {
    private val jwtProvider: JwtProvider? = null

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = getTokenFromRequest(request as HttpServletRequest)
        if (token != null && jwtProvider!!.validateAccessToken(token)) {
            val claims = jwtProvider.getAccessClaims(token)
            val jwtInfoToken = JwtUtils.generate(claims)
            jwtInfoToken.isAuthenticated = true
            SecurityContextHolder.getContext().authentication = jwtInfoToken
        } // TODO: 3/27/23  Убрать метку об авторизации
        chain.doFilter(request, response)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearer = request.getHeader(AUTHORIZATION)
        return if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            bearer.substring(7)
        } else null
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}
