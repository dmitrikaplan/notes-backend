package ru.kaplaan.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.kaplaan.domain.entity.Note

@Repository
@Transactional
interface NoteRepository : CrudRepository<Note, Long> {
    @Modifying
    @Query("update Note u set u.text = :text where u.id = :id")
    fun updateNote(
        @Param("text") text: String?,
        @Param("id") id: Long?
    ): Note?

    fun getNotesByOwner(owner: String?): List<Note>
}
