package ru.kaplaan.domain.exception.notes

class NoteCannotBeAddedException: Exception(){
    override val message: String
        get() = "Ошибка добавления заметки"
}