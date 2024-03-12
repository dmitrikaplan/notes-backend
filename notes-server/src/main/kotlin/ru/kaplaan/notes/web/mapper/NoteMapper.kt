package ru.kaplaan.notes.web.mapper

import ru.kaplaan.notes.domain.entity.Note
import ru.kaplaan.notes.web.dto.NoteDto


fun Note.toDto(): NoteDto {
    return NoteDto(
        title = title,
        text = text
    )
}


fun NoteDto.toEntity(owner: String): Note{
    return Note(
        title = title,
        text = text,
        owner = owner
    )
}


fun List<Note>.toDto(): List<NoteDto>{
    val list = arrayListOf<NoteDto>()

    for(note in this){
        list.add(note.toDto())
    }
    return list
}