package com.thrivematch.ThriveMatch.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Map;

@Service
public class ImageService {
    @Autowired
    private StartUpRepo startUpRepo;
    @Autowired
    private InvestorRepo investorRepo;
    @Autowired
    private IndividualInvestorRepo individualInvestorRepo;
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


    public String retrieveStartUpImage(Integer id) throws IOException {
        Optional<StartUpEntity> fileData = startUpRepo.findById(id);
        return fileData.get().getPicturePath();
    }

    public String retrieveInvestorImage(Integer id) throws IOException {
        Optional<InvestorEntity> fileData = investorRepo.findById(id);
        return fileData.get().getPicturePath();
    }

    public String retrieveIndividualInvestorImage(Integer id) throws IOException {
        Optional<IndividualInvestorEntity> fileData = individualInvestorRepo.findById(id);
        return fileData.get().getPicturePath();
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

