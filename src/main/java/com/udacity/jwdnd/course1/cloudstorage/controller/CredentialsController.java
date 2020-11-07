package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialsController {
    private final UserService userService;
    private final CredentialsService credentialsService;

    public CredentialsController(UserService userService, CredentialsService credentialsService) {
        this.userService = userService;
        this.credentialsService = credentialsService;
    }

    @PostMapping
    public String upsertCredential(Authentication authentication, CredentialForm credentialForm, RedirectAttributes redirectAttributes){
        var userId = this.userService.getUser(authentication.getName()).getUserId();
        this.credentialsService.upsertUserCredential(userId, credentialForm);

        redirectAttributes.addFlashAttribute("currentTab", "credentials");
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteNote(Authentication authentication, @RequestParam("credentialId") Integer credentialId, RedirectAttributes redirectAttributes){
        var userId = this.userService.getUser(authentication.getName()).getUserId();
        this.credentialsService.deleteUserCredentialById(userId, credentialId);

        redirectAttributes.addFlashAttribute("currentTab", "credentials");
        return "redirect:/";
    }
}
