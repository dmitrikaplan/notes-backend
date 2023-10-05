package ru.kaplaan.domain.exception.note

class NoteCannotUpdatedException: Exception(){
    override val message: String
        get() = "Ошибка обновления заметки"
}