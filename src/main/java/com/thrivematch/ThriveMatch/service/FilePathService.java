package com.thrivematch.ThriveMatch.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FilePathService {

    public String saveFile(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        String uploadDirectory = "/home/johnmalek/Desktop/Malek/SpringBoot/ThriveMatch/src/main/java/com/thrivematch/ThriveMatch/files/";
        String filePath = uploadDirectory+fileName;

        file.transferTo(new File(filePath));
        return filePath;
    }
}

