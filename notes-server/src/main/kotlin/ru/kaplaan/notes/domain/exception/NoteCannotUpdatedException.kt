package ru.kaplaan.notes.domain.exception

class NoteCannotUpdatedException: RuntimeException(){
    override val message: String
        get() = "Ошибка обновления заметки"
}