package ru.kaplaan.authserver.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.kaplaan.authserver.domain.entity.RefreshToken
import ru.kaplaan.domain.domain.user.User

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Long>{

    fun findRefreshTokenByUser(user: User): RefreshToken?

    fun findRefreshTokenByToken(token: String): RefreshToken?

    fun deleteByToken(token: String)

   fun existsByToken(token: String): Boolean


}
