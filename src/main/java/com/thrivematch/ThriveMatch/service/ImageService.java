package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.model.IndividualInvestorEntity;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.repository.IndividualInvestorRepo;
import com.thrivematch.ThriveMatch.repository.InvestorRepo;
import com.thrivematch.ThriveMatch.repository.StartUpRepo;
import com.thrivematch.ThriveMatch.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class ImageService {
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









//    // Upload StartUp Image
//    public StartUpEntity uploadStartUpImage(MultipartFile file) throws IOException{
//        StartUpEntity startUp = new StartUpEntity();
//        startUp.setImage(ImageUtils.compressImage(file.getBytes()));
//        return startUpRepo.save(startUp);
//    }
//
//    // Download StartUp Image
//    public byte[] downloadStartUpImage(Integer id) throws IOException {
//        Optional<StartUpEntity> startUp = startUpRepo.findById(id);
//        byte[] startUpImage = startUp.get().getImage();
//        return ImageUtils.decompressImage(startUpImage);
//    }
//
//    // Upload Investor Image
//    public InvestorEntity uploadInvestorImage(MultipartFile file) throws IOException{
//        InvestorEntity investor = new InvestorEntity();
//        investor.setImage(ImageUtils.compressImage(file.getBytes()));
//        return investorRepo.save(investor);
//    }
//
//    // Download Investor Image
//    public byte[] downloadInvestorImage(Integer id) throws IOException {
//        Optional<InvestorEntity> investor = investorRepo.findById(id);
//        byte[] investorImage = investor.get().getImage();
//        return ImageUtils.decompressImage(investorImage);
//    }
//
//    // Upload Individual Investor Image
//    public IndividualInvestorEntity uploadIndividualInvestorImage(MultipartFile file) throws IOException{
//        IndividualInvestorEntity individualInvestor = new IndividualInvestorEntity();
//        individualInvestor.setImage(ImageUtils.compressImage(file.getBytes()));
//        return individualInvestorRepo.save(individualInvestor);
//    }
//
//    // Download Individual Investor Image
//    public byte[] downloadIndividualInvestorImage(Integer id) throws IOException {
//        Optional<IndividualInvestorEntity> individualInvestor =  individualInvestorRepo.findById(id);
//        byte[] individualInvestorImage = individualInvestor.get().getImage();
//        return ImageUtils.decompressImage(individualInvestorImage);
//    }
}

