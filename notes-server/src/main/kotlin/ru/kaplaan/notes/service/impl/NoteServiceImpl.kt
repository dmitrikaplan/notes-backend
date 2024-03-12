package ru.kaplaan.notes.service.impl

import org.springframework.stereotype.Service
import ru.kaplaan.notes.domain.entity.Note
import ru.kaplaan.notes.domain.exception.NoteCannotBeAddedException
import ru.kaplaan.notes.domain.exception.NoteCannotUpdatedException
import ru.kaplaan.notes.repository.NoteRepository
import ru.kaplaan.notes.service.NoteService

// TODO: 3/18/23  Сделать проверка существования заметки перед удалением или обновлением
// TODO: Добавить проверку существования логина в поле owner
@Service
class NoteServiceImpl(
    private val noteRepository: NoteRepository,
) : NoteService {

    override fun addNote(note: Note): Note {
        return noteRepository.save(note)
            ?: throw NoteCannotBeAddedException()
    }

    override fun updateNote(note: Note): Note =
        noteRepository.updateNote(note.text, note.id)
            ?: throw NoteCannotUpdatedException()

    override fun deleteNote(id: Long) =
        noteRepository.deleteById(id)

    override fun getAllNotes(owner: String): List<Note> =
        noteRepository.getNotesByOwner(owner)
}
