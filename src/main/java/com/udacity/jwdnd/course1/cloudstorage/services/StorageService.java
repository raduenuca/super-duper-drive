package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequestMapping("/upload")
public class StorageService {
    private FileMapper fileMapper;

    public StorageService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void addFile(File file) {
        this.fileMapper.addFile(file);
    }

    public List<File> getAllUserFiles(Integer userId) {
        return this.fileMapper.getAllUserFiles(userId);
    }

    public File getUserFileByName(Integer userId, String fileName){
        return this.fileMapper.getUserFileByName(userId, fileName);
    }

    public File getUserFileById(Integer userId, Integer fileId){
        return this.fileMapper.getUserFileById(userId, fileId);
    }

    public void deleteUserFileById(Integer userId, Integer fileId) {
        this.fileMapper.deleteUserFileById(userId, fileId);
    }
}
