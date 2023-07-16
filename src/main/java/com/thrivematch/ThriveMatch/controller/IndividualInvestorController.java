package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.IndividualInvestorEntity;
import com.thrivematch.ThriveMatch.repository.IndividualInvestorRepo;
import com.thrivematch.ThriveMatch.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class IndividualInvestorController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private IndividualInvestorRepo individualInvestorRepo;


    // Upload investor information
    @PostMapping("/individual_investor")
    public ResponseEntity<SuccessAndMessage> createProfile(
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("image" ) MultipartFile file, @RequestHeader(name="Authorization") String token){
        SuccessAndMessage response = new SuccessAndMessage();
        try{
            byte[] image = imageService.uploadIndividualInvestorImage(file).getImage();
            IndividualInvestorEntity profile = new IndividualInvestorEntity();
            profile.setName(name);
            profile.setEmail(email);
            profile.setDescription(description);
            profile.setIndustry(industry);
            profile.setImage(image);
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

    // Return the image belonging to a specific individual investor
    @GetMapping("/individual_investor/{individualinvestorId}/image")
    public ResponseEntity<?> retrieveIndividualInvestorImage(@PathVariable Integer individualinvestorId) throws IOException{
        byte[] imageData = imageService.downloadIndividualInvestorImage(individualinvestorId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
}
