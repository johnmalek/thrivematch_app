package com.thrivematch.ThriveMatch.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

@Service
public class DocumentService {
    @Autowired
    private DocumentsRepo documentsRepo;
    @Autowired
    private Cloudinary cloudinaryConfig;


    public String uploadFile(MultipartFile file) {
        try {
            File uploadedFile = convertMultiPartToFile(file);
            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            boolean isDeleted = uploadedFile.delete();

            if (isDeleted){
                System.out.println("File successfully deleted");
            }else
                System.out.println("File doesn't exist");
            return  uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

//    public String saveFile(MultipartFile file) throws IOException {
//        String fileName = file.getOriginalFilename();
//        String uploadDirectory = "/home/johnmalek/Desktop/Malek/SpringBoot/ThriveMatch/src/main/java/com/thrivematch/ThriveMatch/StartUpFiles/";
//        String filePath = uploadDirectory+fileName;
//
//        file.transferTo(new File(filePath));
//        return filePath;
//    }
//
//    public byte[] downloadFile(Integer id) throws IOException {
//        Optional<DocumentsEntity> file = documentsRepo.findById(id);
//        String filePath = file.get().getFilePath();
//        return Files.readAllBytes(new File(filePath).toPath());
//    }
}
