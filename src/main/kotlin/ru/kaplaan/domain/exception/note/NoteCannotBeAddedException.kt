package ru.kaplaan.domain.exception.note

class NoteCannotBeAddedException: Exception(){
    override val message: String
        get() = "Ошибка добавления заметки"
}