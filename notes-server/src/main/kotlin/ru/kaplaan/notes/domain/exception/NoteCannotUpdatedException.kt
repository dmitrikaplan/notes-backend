package ru.kaplaan.notes.domain.exception

class NoteCannotUpdatedException: NoteException("Ошибка обновления заметки"){
    override val message: String
        get() = super.message
}