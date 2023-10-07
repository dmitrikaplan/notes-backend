package ru.kaplaan.domain.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.kaplaan.domain.entity.User
import ru.kaplaan.service.CryptoService
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider (private val cryptoService: CryptoService) {

    private lateinit var jwtAccessToken: SecretKey
    private lateinit var jwtRefreshToken: SecretKey

    private val log = LoggerFactory.getLogger(JwtProvider::class.java)

    private fun setJwtAccessToken(user: User?) {
        val data = cryptoService.getIntoBase64(user!!)
        jwtAccessToken = Keys.hmacShaKeyFor(Decoders.BASE64.decode(data))
    }

    private fun setJwtRefreshToken(user: User?) {
        val data = cryptoService.getIntoBase64(user!!)
        jwtRefreshToken = Keys.hmacShaKeyFor(Decoders.BASE64.decode(data))
    }

    fun generateJwtAccessToken(user: User): String {
        setJwtAccessToken(user)
        val now = LocalDateTime.now()
        val accessExpirationInstant = now.plusHours(1).atZone(ZoneId.systemDefault()).toInstant()
        val accessExpiration = Date.from(accessExpirationInstant)
        return Jwts.builder().setSubject(user.login).setExpiration(accessExpiration).signWith(jwtAccessToken)
            .compact()
    }

    fun generateJwtRefreshToken(user: User): String {
        setJwtRefreshToken(user)
        val now = LocalDateTime.now()
        val refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant()
        val refreshExpiration = Date.from(refreshExpirationInstant)
        return Jwts.builder().setExpiration(refreshExpiration).setSubject(user.login).signWith(jwtRefreshToken)
            .compact()
    }

    fun validateAccessToken(accessToken: String): Boolean {
        return validateToken(accessToken, jwtAccessToken!!)
    }

    fun validateRefreshToken(refreshToken: String): Boolean {
        return validateToken(refreshToken, jwtRefreshToken!!)
    }

    private fun validateToken(token: String, key: Key): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: ExpiredJwtException) {
            println("Error")
            log.error("Token expired", e)
        } catch (e: UnsupportedJwtException) {
            log.error("Unsupported jwt", e)
            println("Error")
        } catch (e: MalformedJwtException) {
            log.error("Malformed jwt", e)
            println("Error")
        } catch (e: SignatureException) {
            log.error("Invalid signature", e)
            println("Error")
        } catch (e: Exception) {
            log.error("invalid token", e)
            println("Error")
        }
        return false
    }

    fun getAccessClaims(accessToken: String): Claims {
        return getClaims(accessToken, jwtAccessToken!!)
    }

    fun getRefreshClaims(refreshToken: String): Claims {
        return getClaims(refreshToken, jwtRefreshToken!!)
    }

    private fun getClaims(token: String, key: Key): Claims {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
    }
}
