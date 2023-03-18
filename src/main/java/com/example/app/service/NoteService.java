package com.example.app.service;

import com.example.app.utils.model.entities.Note;
import lombok.NonNull;

import java.util.List;

/*
После добавления заметки пользователь получает обратно эту класс с данной заметкой
 и заполненным id, который будет отправлять для удаления и обновления существующей заметки
 */


public interface NoteService {

    Note addNote(@NonNull Note note);

    Note updateNote(@NonNull Note note);

    void deleteNote(@NonNull Long id);

    List<Note> getAllNotes();
}
