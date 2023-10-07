package ru.kaplaan.service.impl

import org.springframework.stereotype.Service
import ru.kaplaan.domain.entity.Note
import ru.kaplaan.domain.exception.note.NoteCannotBeAddedException
import ru.kaplaan.domain.exception.note.NoteCannotUpdatedException
import ru.kaplaan.repository.NoteRepository
import ru.kaplaan.service.AuthService
import ru.kaplaan.service.NoteService
import ru.kaplaan.web.controller.AuthController

// TODO: 3/18/23  Сделать проверка существования заметки перед удалением или обновлением
// TODO: Добавить проверку существования логина в поле owner
@Service
class NoteServiceImpl(
    private val noteRepository: NoteRepository,
    private val authService: AuthService
) : NoteService {

    override fun addNote(note: Note): Note {
        return noteRepository.save(note) ?: throw NoteCannotBeAddedException()
    }

    override fun updateNote(note: Note): Note {
        return noteRepository.updateNote(note.text, note.id) ?: throw NoteCannotUpdatedException()
    }

    override fun deleteNote(id: Long) {
        noteRepository.deleteById(id)
    }

    override fun allNotes(): List<Note> {
        // TODO: добавить owner 
        val owner = ""
        return noteRepository.getNotesByOwner(owner)
    }
}
