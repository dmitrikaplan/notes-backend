package com.example.repository

import com.example.utils.model.entities.Salt
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SaltRepository : CrudRepository<Salt, Long?> {
    fun getSaltByOwner(owner: String): Salt?
}
