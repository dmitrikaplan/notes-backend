package ru.kaplaan.dto.mapper;

import org.mapstruct.Mapper;
import ru.kaplaan.domain.entity.Note;
import ru.kaplaan.web.dto.note.NoteDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteDto toDto(Note note);

    List<NoteDto> toDto(List<Note> notes);

    Note toEntity(NoteDto noteDto);


}



