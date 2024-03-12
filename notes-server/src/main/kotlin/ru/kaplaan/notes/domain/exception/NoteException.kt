package ru.kaplaan.notes.domain.exception

abstract class NoteException(override val message: String): RuntimeException(message)