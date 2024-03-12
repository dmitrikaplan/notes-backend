package ru.kaplaan.authserver.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.kaplaan.authserver.domain.entity.RefreshToken

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Long>
