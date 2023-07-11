package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.InvestorInfoResponse;
import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.repository.InvestorRepo;
import com.thrivematch.ThriveMatch.service.FileService;
import com.thrivematch.ThriveMatch.service.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
public class InvestorController {
    @Autowired
    private FileService fileService;
    @Autowired
    private InvestorRepo investorRepo;
    @Autowired
    private InvestorService investorService;

    @PostMapping("/investors")
    public ResponseEntity<SuccessAndMessage> createProfile(
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("address") String address,
            @RequestPart("poBox") String poBox,
            @RequestPart("year") String year,
            @RequestPart("file" ) MultipartFile file, @RequestHeader(name="Authorization") String token){
        SuccessAndMessage response = new SuccessAndMessage();
        if(investorRepo.existsByName(name)){
            response.setSuccess(false);
            response.setMessage("Investor with name "+ name + " already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            String picturePath = fileService.saveFile(file);
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

    @GetMapping("all_investors")
    public ResponseEntity<InvestorInfoResponse> getAllInvestors(){
        return investorService.getAllInvestors();
    }

    @GetMapping("/investor/{investorId}/image")
    public ResponseEntity<?> retrieveInvestorImage(@PathVariable Integer investorId) throws IOException{
        byte[] imageData = fileService.retrieveInvestorImage(investorId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
}
