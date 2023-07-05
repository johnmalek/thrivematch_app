package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.repository.InvestorRepo;
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
public class InvestorController {
    @Autowired
    private FilePathService filePathService;
    @Autowired
    private InvestorRepo investorRepo;

    @PostMapping("/investors")
    public ResponseEntity<SuccessAndMessage> createProfile(
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("address") String address,
            @RequestPart("poBox") String poBox,
            @RequestPart("year") String year,
            @RequestPart("pictureFile" ) MultipartFile file){
        SuccessAndMessage response = new SuccessAndMessage();
        try{
            String picturePath = filePathService.saveFile(file);
            InvestorEntity profile = new InvestorEntity();
            profile.setName(name);
            profile.setEmail(email);
            profile.setDescription(description);
            profile.setIndustry(industry);
            profile.setPoBox(poBox);
            profile.setAddress(address);
            profile.setYearFounded(LocalDate.parse(year));
            profile.setPicturePath(picturePath);
            InvestorEntity savedProfile = investorRepo.save(profile);
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
