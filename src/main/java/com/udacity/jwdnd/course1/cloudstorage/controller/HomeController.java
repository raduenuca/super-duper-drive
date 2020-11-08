package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.StorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private final StorageService storageService;
    private final NotesService notesService;
    private final CredentialsService credentialsService;
    private final UserService userService;

    public HomeController(StorageService storageService, NotesService notesService, CredentialsService credentialsService, UserService userService) {
        this.storageService = storageService;
        this.notesService = notesService;
        this.credentialsService = credentialsService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication,  Model model){
        var userId = this.userService.getUser(authentication.getName()).getUserId();

        model.addAttribute("userName", authentication.getName());
        model.addAttribute("uploadedFiles", this.storageService.getAllUserFiles(userId));
        model.addAttribute("notes", this.notesService.getAllUserNotes(userId));
        model.addAttribute("credentials", this.credentialsService.getAllUserCredentials(userId));

        return "home";
    }
}
