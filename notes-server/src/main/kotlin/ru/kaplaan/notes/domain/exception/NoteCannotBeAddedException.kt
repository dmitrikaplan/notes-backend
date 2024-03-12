package ru.kaplaan.notes.domain.exception

class NoteCannotBeAddedException: NoteException("Ошибка добавления заметки"){
    override val message: String
        get() = super.message
}