package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class NotesService {
    private NotesMapper notesMapper;

    public NotesService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public void upsertUserNote(Integer userId, NoteForm noteForm) {
        var note = this.notesMapper.getUserNoteById(userId, noteForm.getNoteId());

        if (note == null) {
            note = new Note(null, noteForm.getNoteTitle(), noteForm.getNoteDescription(), userId);
            this.notesMapper.addNote(note);
        } else {
            note.setNoteTitle(noteForm.getNoteTitle());
            note.setNoteDescription(noteForm.getNoteDescription());

            this.notesMapper.updateNote(note);
        }
    }

    public List<Note> getAllUserNotes(Integer userId){
        return this.notesMapper.getAllUserNotes(userId);
    }

    public void deleteUserNoteById(Integer userId, Integer noteId){
        this.notesMapper.deleteUserNoteById(userId, noteId);
    }
}
