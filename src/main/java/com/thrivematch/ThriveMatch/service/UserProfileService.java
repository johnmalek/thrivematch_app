package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.dto.UserProfileDto;
import com.thrivematch.ThriveMatch.model.UserProfileEntity;
import com.thrivematch.ThriveMatch.repository.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UserProfileService {

    public String saveFile(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        String uploadDirectory = "/home/johnmalek/Desktop/Malek/SpringBoot/ThriveMatch/src/main/resources/static/Files/";
        String filePath = uploadDirectory+fileName;

        file.transferTo(new File(filePath));
        return filePath;
    }
}

