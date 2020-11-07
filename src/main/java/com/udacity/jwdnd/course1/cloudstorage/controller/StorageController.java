package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.StorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class StorageController {
    private final StorageService storageService;
    private final UserService userService;

    public StorageController(StorageService storageService, UserService userService) {
        this.storageService = storageService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String upload(Authentication authentication, @RequestParam("fileUpload") MultipartFile uploadedFile, RedirectAttributes redirectAttributes) {
        var userId = this.userService.getUser(authentication.getName()).getUserId();

        if (uploadedFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload");
            return "redirect:/";
        }

        var file = this.storageService.getUserFileByName(userId, uploadedFile.getOriginalFilename());

        if (file != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "A file with the same name already exists");
            return "redirect:/";
        }

        try {
            file = new File(
                    null,
                    uploadedFile.getOriginalFilename(),
                    uploadedFile.getContentType(),
                    Long.toString(uploadedFile.getSize()),
                    userId,
                    uploadedFile.getBytes()
            );

            storageService.addFile(file);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    String.format("You successfully uploaded '%s'", uploadedFile.getOriginalFilename())
            );
        } catch (IOException e) {
            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    String.format("An error occurred during file upload: '%s. Please try again!", e.getMessage())
            );

        }

        return "redirect:/";
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getFile(Authentication authentication, @PathVariable Integer fileId) {
        var userId = this.userService.getUser(authentication.getName()).getUserId();
        var file = this.storageService.getUserFileById(userId, fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", file.getFileName()))
                .body(file.getFileData());
    }

    @PostMapping("/delete")
    public String deleteFile(Authentication authentication, @RequestParam("fileId") Integer fileId, RedirectAttributes redirectAttributes) {
        var userId = this.userService.getUser(authentication.getName()).getUserId();
        var fileName = this.storageService.getUserFileById(userId, fileId).getFileName();

        this.storageService.deleteUserFileById(userId, fileId);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                String.format("You successfully deleted '%s'", fileName)
        );

        return "redirect:/";
    }

}
