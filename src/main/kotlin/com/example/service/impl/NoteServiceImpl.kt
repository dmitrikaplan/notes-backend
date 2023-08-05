package com.example.service.impl

import com.example.repository.NoteRepository
import com.example.service.NoteService
import com.example.utils.exceptions.notesExceptions.NoteCannotBeAddedException
import com.example.utils.exceptions.notesExceptions.NoteCannotUpdatedException
import com.example.utils.dto.entities.Note
import org.springframework.stereotype.Service

// TODO: 3/18/23  Сделать проверка существования заметки перед удалением или обновлением
@Service
class NoteServiceImpl(
    private val noteRepository: NoteRepository
) : NoteService {

    override fun addNote(note: Note): Note {
        return noteRepository.save(note) ?: throw NoteCannotBeAddedException()
    }

    override fun updateNote(note: Note): Note{
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
