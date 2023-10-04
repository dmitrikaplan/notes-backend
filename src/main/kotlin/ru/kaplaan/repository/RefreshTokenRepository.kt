package ru.kaplaan.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.kaplaan.domain.entity.RefreshToken

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Long>
