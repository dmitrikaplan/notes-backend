package ru.kaplaan.domain.exception.notes

class NoteCannotUpdatedException: Exception(){
    override val message: String
        get() = "Ошибка обновления заметки"
}