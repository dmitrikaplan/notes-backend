package com.example.service

import com.example.utils.dto.entities.Note
import org.springframework.stereotype.Service

/*
После добавления заметки пользователь получает обратно эту класс с данной заметкой
и заполненным id, который будет отправлять для удаления и обновления существующей заметки
*/
@Service
interface NoteService {
    fun addNote(note: Note): Note
    fun updateNote(note: Note): Note
    fun deleteNote(id: Long)

    fun allNotes(): List<Note>
}
