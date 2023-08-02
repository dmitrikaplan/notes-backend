package com.example.controller

import com.example.service.NoteService
import com.example.utils.model.entities.Note
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/notes")
class NoteController (
    private val noteService: NoteService
){
    @PostMapping("add")
    fun addNotes(@RequestBody note: Note?): ResponseEntity<Note> {
        val note1 = noteService.addNote(note!!)
        return ResponseEntity.status(HttpStatus.OK).body(note1)
    }

    @PostMapping("update")
    fun updateNote(@RequestBody note: Note?): ResponseEntity<Note> {
        val note1 = noteService.updateNote(note!!)
        return ResponseEntity.status(HttpStatus.OK).body(note1)
    }

    @DeleteMapping("delete")
    fun deleteNote(@RequestBody id: Long?): ResponseEntity<String> {
        noteService.deleteNote(id!!)
        return ResponseEntity.status(HttpStatus.OK).body("Заметка успешно удалена")
    }

    @GetMapping("get")
    fun getNotes(): ResponseEntity<List<Note>>{
        val notes = noteService.allNotes()
        return ResponseEntity.status(HttpStatus.OK).body(notes)
    }
}
