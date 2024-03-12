package ru.kaplaan.notes.service

import org.springframework.stereotype.Service
import ru.kaplaan.notes.domain.entity.Note

/*
После добавления заметки пользователь получает обратно эту класс с данной заметкой
и заполненным id, который будет отправлять для удаления и обновления существующей заметки
*/
@Service
interface NoteService {
    fun addNote(note: Note): Note

    fun updateNote(note: Note): Note

    fun deleteNote(id: Long)

    fun getAllNotes(owner: String): List<Note>
}
