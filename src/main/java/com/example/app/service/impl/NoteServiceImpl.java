package com.example.app.service.impl;

import com.example.app.repository.NoteRepository;
import com.example.app.service.NoteService;
import com.example.app.utils.model.entities.Note;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;


// TODO: 3/18/23  Сделать проверка существования заметки перед удалением или обновлением
@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {

    NoteRepository noteRepository;
    @Override
    public Note addNote(@NonNull Note note) {
        return noteRepository.save(note);

    }

    @Override
    public Note updateNote(@NonNull Note note) {
        return noteRepository.updateNote(note.getText(), note.getId());
    }

    @Override
    public void deleteNote(@NonNull Long id) {
        noteRepository.deleteById(id);
    }

    @Override
    public List<Note> getAllNotes() {
        String owner = "";
        return noteRepository.getNotesByOwner(owner);
    }
}
