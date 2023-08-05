package com.example.utils.exceptions.notesExceptions

class NoteCannotUpdatedException: Exception(){
    override val message: String
        get() = "Ошибка обновления заметки"
}