package ru.kaplaan.service

import io.jsonwebtoken.io.Encoders
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.kaplaan.domain.entity.Salt
import ru.kaplaan.domain.entity.User
import ru.kaplaan.domain.exception.user.UserNotFoundException
import ru.kaplaan.repository.SaltRepository
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

@Service
class CryptoService(private val saltRepository: SaltRepository) {

    private val log = LoggerFactory.getLogger(CryptoService::class.java)

    fun getHash(login: String, password: String): String {
        val salts: Salt = saltRepository.getSaltByOwner(login)
            ?: throw UserNotFoundException("Пользователь с данным логином и паролем не найден")
        val salt1 = salts.salt1
        val salt2 = salts.salt2
        val hash = md5(salt1 + password + salt2)
        return md5(hash)
    }

    fun doHash(user: User): String {
        val salt1 = generateSalt(20)
        val salt2 = generateSalt(20)
        val salt = Salt(salt1, salt2, user.login)
        saltRepository.save(salt)

        val stringBuilder = StringBuilder()
        stringBuilder.append(salt1).append(user.password).append(salt2)

        val hash = md5(stringBuilder.toString())
        return md5(hash)
    }

    private fun md5(password: String): String {
        return try {
            val md = MessageDigest.getInstance("MD5")
            BigInteger(1, md.digest(password.toByteArray())).toString(16)
        } catch (e: NoSuchAlgorithmException) {
            log.error("Не найден алгоритм шифрования")
            ""
        }
    }

    private fun generateSalt(saltSize: Int): String {
        val sb = StringBuilder()
        val random = Random()
        for (i in 0 until saltSize) {
            sb.append(random.nextInt(26) + 'a'.code)
        }
        return sb.toString()
    }

    fun generateKeyForJwt(user: User): String {
        val salt1 = generateSalt(120)
        val salt2 = generateSalt(120)

        val stringBuilder = StringBuilder()
        stringBuilder.append(salt1).append(user.password).append(user.login).append(salt2)

        return md5(md5(stringBuilder.toString()))
    }

    fun getIntoBase64(user: User): String {
        val salt1 = generateSalt(256)
        val salt2 = generateSalt(256)
        val salt3 = generateSalt(30)

        val stringBuilder1 = StringBuilder()
            .append(salt1).append(user.password)

        val stringBuilder2 = StringBuilder()
            .append(salt2).append(user.login).append(md5(stringBuilder1.toString()))

        val secret = StringBuilder()
            .append(md5(stringBuilder2.toString())).append(salt3).toString()

        return Encoders.BASE64.encode(secret.toByteArray())
    }
}
