package com.example.utils.exceptions.notesExceptions

class NoteCannotBeAddedException: Exception(){
    override val message: String
        get() = "Ошибка добавления заметки"
}