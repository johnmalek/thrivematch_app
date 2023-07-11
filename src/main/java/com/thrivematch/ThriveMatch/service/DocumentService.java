package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.DocumentsEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.repository.DocumentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class DocumentService {
    @Autowired
    private DocumentsRepo documentsRepo;

    public String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String uploadDirectory = "/home/johnmalek/Desktop/Malek/SpringBoot/ThriveMatch/src/main/java/com/thrivematch/ThriveMatch/StartUpFiles/";
        String filePath = uploadDirectory+fileName;

        file.transferTo(new File(filePath));
        return filePath;
    }

    public byte[] downloadFile(Integer id) throws IOException {
        Optional<DocumentsEntity> file = documentsRepo.findById(id);
        String filePath = file.get().getFilePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
