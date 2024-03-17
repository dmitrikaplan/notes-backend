package ru.kaplaan.api.domain.auth

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService {

    @Value("\${jwt.access.secret}")
    private lateinit var jwtAccessSecret: String

    @Value("\${jwt.refresh.secret}")
    private lateinit var jwtRefreshSecret: String

    private val log = LoggerFactory.getLogger(javaClass)


    private fun getAccessSignKey(): Key =
        Decoders.BASE64.decode(jwtAccessSecret).let { bytes ->
            Keys.hmacShaKeyFor(bytes)
        }

    private fun getRefreshSignKey(): Key =
        Decoders.BASE64.decode(jwtRefreshSecret).let { bytes ->
            Keys.hmacShaKeyFor(bytes)
        }


    fun isValidAccessToken(accessToken: String): Boolean =
        validateToken(accessToken, getAccessSignKey()) && isNotExpired(accessToken, getAccessSignKey())


    fun isValidRefreshToken(refreshToken: String): Boolean =
        validateToken(refreshToken, getRefreshSignKey()) && isNotExpired(refreshToken, getRefreshSignKey())

    private fun validateToken(token: String, key: Key): Boolean {
        try {
            Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            return true

        } catch (e: ExpiredJwtException) {
            log.error("Token expired")
        } catch (e: UnsupportedJwtException) {
            log.error("Unsupported jwt")
        } catch (e: MalformedJwtException) {
            log.error("Malformed jwt")
        } catch (e: SignatureException) {
            log.error("Invalid signature")
        } catch (e: Exception) {
            log.error("invalid token")
        }

        return false
    }

    private fun isNotExpired(jwtToken: String, key: Key): Boolean =
        extractExpiration(jwtToken, key).after(Date(System.currentTimeMillis()))


    fun extractUsernameFromAccessToken(jwtToken: String): String =
        extractClaim(jwtToken, getAccessSignKey()){
                it["username"] as String
    }

    fun extractPasswordFromAccessToken(jwtToken: String): String =
        extractClaim(jwtToken, getAccessSignKey()) {
            it["password"] as String
        }


    private fun extractExpiration(jwtToken: String, key: Key): Date =
        extractClaim(jwtToken, key, Claims::getExpiration)


    private fun <T> extractClaim(jwtToken: String, key: Key, resolver: (Claims) -> T): T =
        resolver(extractAllClaims(jwtToken, key))


    private fun extractAllClaims(jwtToken: String, key: Key): Claims =
            Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwtToken)
            .body

}
