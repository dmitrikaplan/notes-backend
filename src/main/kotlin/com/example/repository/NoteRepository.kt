package com.example.repository

import com.example.utils.model.entities.Note
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface NoteRepository : CrudRepository<Note, Long> {
    @Modifying
    @Query("update Note u set u.text = :text where u.id = :id")
    fun updateNote(
        @Param("text") text: String?,
        @Param("id") id: Long?
    ): Note?

    fun getNotesByOwner(owner: String?): List<Note>?
}
