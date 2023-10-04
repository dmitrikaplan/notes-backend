package ru.kaplaan.service.impl

import org.springframework.stereotype.Service
import ru.kaplaan.domain.entity.Note
import ru.kaplaan.domain.exception.notes.NoteCannotBeAddedException
import ru.kaplaan.domain.exception.notes.NoteCannotUpdatedException
import ru.kaplaan.repository.NoteRepository
import ru.kaplaan.service.NoteService

// TODO: 3/18/23  Сделать проверка существования заметки перед удалением или обновлением
@Service
class NoteServiceImpl(
    private val noteRepository: NoteRepository
) : NoteService {

    override fun addNote(note: Note): Note {
        return noteRepository.save(note) ?: throw NoteCannotBeAddedException()
    }

    override fun updateNote(note: Note): Note {
        return noteRepository.updateNote(note.getText(), note.getId()) ?: throw NoteCannotUpdatedException()
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
