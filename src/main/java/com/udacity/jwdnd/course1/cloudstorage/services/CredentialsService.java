package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
public class CredentialsService {
    private final CredentialsMapper credentialsMapper;
    private final EncryptionService encryptionService;

    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    public void deleteUserCredentialById(Integer userId, Integer credentialId) {
        this.credentialsMapper.deleteUserCredentialById(userId, credentialId);
    }

    public void upsertUserCredential(Integer userId, CredentialForm credentialForm) {
        var credential = this.credentialsMapper.getUserCredentialById(userId, credentialForm.getCredentialId());

        if (credential == null) {
            var random = new SecureRandom();
            var key = new byte[16];
            random.nextBytes(key);

            var encryptionKey = Base64.getEncoder().encodeToString(key);
            var encodedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encryptionKey);

            credential = new Credential(null, credentialForm.getUrl(), credentialForm.getUsername(), encryptionKey, encodedPassword, userId);
            this.credentialsMapper.addCredential(credential);
        } else {
            credential.setUrl(credentialForm.getUrl());
            credential.setUsername(credentialForm.getUsername());
            credential.setPassword(encryptionService.encryptValue(credentialForm.getPassword(), credential.getKey()));

            this.credentialsMapper.updateCredential(credential);
        }
    }

    public List<CredentialForm> getAllUserCredentials(Integer userId) {
        return this.credentialsMapper.getAllUserCredentials(userId).stream()
                .filter(Objects::nonNull)
                .map(this::fromCredential)
                .collect(toList());
    }

    private CredentialForm fromCredential(Credential credential){
        var credentialForm = new CredentialForm();
        credentialForm.setCredentialId(credential.getCredentialId());
        credentialForm.setUrl(credential.getUrl());
        credentialForm.setUsername(credential.getUsername());
        credentialForm.setEncryptedPassword(credential.getPassword());
        credentialForm.setPassword(this.encryptionService.decryptValue(credential.getPassword(), credential.getKey()));

        return credentialForm;
    }
}
