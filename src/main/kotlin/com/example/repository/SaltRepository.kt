package com.example.repository

import com.example.utils.dto.entities.Salt
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SaltRepository : CrudRepository<Salt, Long?> {
    fun getSaltByOwner(owner: String): Salt?
}
