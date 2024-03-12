package ru.kaplaan.notes.domain.exception

class NoteCannotBeAddedException: RuntimeException(){
    override val message: String
        get() = "Ошибка добавления заметки"
}