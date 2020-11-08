package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NotesController {
    private final UserService userService;
    private final NotesService notesService;

    public NotesController(UserService userService, NotesService notesService) {
        this.userService = userService;
        this.notesService = notesService;
    }

    @PostMapping
    public String upsertNote(Authentication authentication,
                             RedirectAttributes redirectAttributes,
                             NoteForm noteForm) {
        try {
            var userId = this.userService.getUser(authentication.getName()).getUserId();
            this.notesService.upsertUserNote(userId, noteForm);

            redirectAttributes.addFlashAttribute(
                    "notesSuccessMessage",
                    String.format("Note %s successfully!", noteForm.getNoteId() == null ? "created" : "edited"));
        } catch(Exception ex){
            redirectAttributes.addFlashAttribute(
                    "notesErrorMessage",
                    String.format("Error %s note!", noteForm.getNoteId() == null ? "creating" : "updating"));
        }

        redirectAttributes.addFlashAttribute("currentTab", "notes");
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteNote(Authentication authentication,
                             RedirectAttributes redirectAttributes,
                             @RequestParam("noteId") Integer noteId){
        try {
            var userId = this.userService.getUser(authentication.getName()).getUserId();
            this.notesService.deleteUserNoteById(userId, noteId);

            redirectAttributes.addFlashAttribute("notesSuccessMessage", "Note deleted successfully!");
        } catch(Exception ex) {
            redirectAttributes.addFlashAttribute("notesErrorMessage", "Error deleting note!");
        }

        redirectAttributes.addFlashAttribute("currentTab", "notes");
        return "redirect:/";
    }
}
