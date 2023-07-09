package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.IndividualInvestorEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.repository.IndividualInvestorRepo;
import com.thrivematch.ThriveMatch.service.FilePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
public class IndividualInvestorController {
    @Autowired
    private FilePathService filePathService;
    @Autowired
    private IndividualInvestorRepo individualInvestorRepo;

    @PostMapping("/individual_investor")
    public ResponseEntity<SuccessAndMessage> createProfile(
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("file" ) MultipartFile file, @RequestHeader(name="Authorization") String token){
        SuccessAndMessage response = new SuccessAndMessage();
        try{
            String picturePath = filePathService.saveFile(file);
            IndividualInvestorEntity profile = new IndividualInvestorEntity();
            profile.setName(name);
            profile.setEmail(email);
            profile.setDescription(description);
            profile.setIndustry(industry);
            profile.setPicturePath(picturePath);
            IndividualInvestorEntity savedProfile = individualInvestorRepo.save(profile);
            response.setSuccess(true);
            response.setMessage("Profile created successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e){
            response.setSuccess(false);
            response.setMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
