package com.example.app.repository;

import com.example.app.utils.model.entities.Note;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Modifying
    @Query("update Note u set u.text = :text where u.id = :id")
    Note updateNote(
            @Param("text") String text,
            @Param("id") Long id
    );
    List<Note> getNotesByOwner(String owner);
}
