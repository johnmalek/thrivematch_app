package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.model.IndividualInvestorEntity;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.repository.IndividualInvestorRepo;
import com.thrivematch.ThriveMatch.repository.InvestorRepo;
import com.thrivematch.ThriveMatch.repository.StartUpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private StartUpRepo startUpRepo;
    @Autowired
    private InvestorRepo investorRepo;
    @Autowired
    private IndividualInvestorRepo individualInvestorRepo;

    public String saveFile(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        String uploadDirectory = "/home/johnmalek/Desktop/Malek/SpringBoot/ThriveMatch/src/main/java/com/thrivematch/ThriveMatch/files/";
        String filePath = uploadDirectory+fileName;

        file.transferTo(new File(filePath));
        return filePath;
    }

    public byte[] retrieveStartUpImage(Integer id) throws IOException {
        Optional<StartUpEntity> fileData = startUpRepo.findById(id);
        String filePath = fileData.get().getPicturePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }

    public byte[] retrieveInvestorImage(Integer id) throws IOException {
        Optional<InvestorEntity> fileData = investorRepo.findById(id);
        String filePath = fileData.get().getPicturePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }

    public byte[] retrieveIndividualInvestorImage(Integer id) throws IOException {
        Optional<IndividualInvestorEntity> fileData = individualInvestorRepo.findById(id);
        String filePath = fileData.get().getPicturePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }
}

