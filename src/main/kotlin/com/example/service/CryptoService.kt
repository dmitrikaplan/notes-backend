package com.example.service

import com.example.repository.SaltRepository
import com.example.utils.exceptions.UserNotFoundException
import com.example.utils.model.entities.Salt
import com.example.utils.model.entities.User
import io.jsonwebtoken.io.Encoders
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

@Service
class CryptoService(private val saltRepository: SaltRepository) {

    private val log = LoggerFactory.getLogger(CryptoService::class.java)

    fun getHash(user: User): String {
        val salts: Salt = saltRepository.getSaltByOwner(user.getLogin()!!)
            ?: throw UserNotFoundException("Пользователь с данным логином и паролем не найден")
        val salt1 = salts.getSalt1()
        val salt2 = salts.getSalt2()
        val hash = md5(salt1 + user.getPassword() + salt2)
        return md5(hash)
    }

    fun doHash(user: User): String {
        val salt1 = generateSalt(20)
        val salt2 = generateSalt(20)
        val salt = Salt(salt1, salt2, user.getLogin()!!)
        saltRepository.save(salt)
        val hash = md5(salt1 + user.getPassword() + salt2)
        return md5(hash)
    }

    private fun md5(password: String): String {
        return try {
            val md = MessageDigest.getInstance("MD5")
            BigInteger(1, md.digest(password.toByteArray())).toString(16)
        } catch (e: NoSuchAlgorithmException) {
            log.error("не найден алгоритм шифрования")
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
        return md5(md5(salt1 + user.getPassword()) + user.getLogin() + salt2)
    }

    fun getIntoBase64(user: User): String {
        val salt1 = generateSalt(256)
        val salt2 = generateSalt(256)
        val salt3 = generateSalt(30)
        val secret = md5(salt2 + user.getLogin() + md5(salt1 + user.getPassword())) + salt3
        return Encoders.BASE64.encode(secret.toByteArray())
    }
}
