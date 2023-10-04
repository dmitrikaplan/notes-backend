package ru.kaplaan.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.kaplaan.domain.entity.Salt

@Repository
interface SaltRepository : CrudRepository<Salt, Long?> {
    fun getSaltByOwner(owner: String): Salt?
}
