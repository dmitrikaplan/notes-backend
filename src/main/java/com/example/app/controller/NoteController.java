package com.example.app.controller;

import com.example.app.service.NoteService;
import com.example.app.utils.model.entities.Note;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
@AllArgsConstructor
public class NoteController {

    private NoteService noteService;

    @PostMapping("add")
    public ResponseEntity<Note> addNotes(@RequestBody Note note){
        Note note1 = noteService.addNote(note);
       return ResponseEntity.status(HttpStatus.OK).body(note1);
    }

    @PostMapping("update")
    public ResponseEntity<Note> updateNote(@RequestBody Note note){
        Note note1 = noteService.updateNote(note);
        return ResponseEntity.status(HttpStatus.OK).body(note1);
    }

    @PostMapping("delete")
    public ResponseEntity<String> deleteNote(@RequestBody Long id){
        noteService.deleteNote(id);
        return ResponseEntity.status(HttpStatus.OK).body("Заметка успешно удалена");
    }

    @GetMapping("get")
    public ResponseEntity<List<Note>> getNote(){
        List<Note> notes = noteService.getAllNotes();
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }
}
