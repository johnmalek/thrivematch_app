package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.IndividualInvestorEntity;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.model.UserEntity;
import com.thrivematch.ThriveMatch.repository.IndividualInvestorRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import com.thrivematch.ThriveMatch.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class IndividualInvestorController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private IndividualInvestorRepo individualInvestorRepo;
    @Autowired
    private UserRepo userRepo;


    // Upload investor information
    @PostMapping("/individual_investor")
    public ResponseEntity<SuccessAndMessage> createProfile(
            Principal principal,
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("image" ) MultipartFile file, @RequestHeader(name="Authorization") String token){
        SuccessAndMessage response = new SuccessAndMessage();
        try{
            String username = principal.getName();

            UserEntity user = userRepo.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User email " + username + " not found"));

            if (individualInvestorRepo.existsByName(name)) {
                response.setSuccess(false);
                response.setMessage("investor already exists");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            String picture = imageService.saveFile(file);
            IndividualInvestorEntity individualInvestor = new IndividualInvestorEntity();
            individualInvestor.setName(name);
            individualInvestor.setEmail(email);
            individualInvestor.setDescription(description);
            individualInvestor.setIndustry(industry);
            individualInvestor.setPicturePath(picture);
            individualInvestor.setUser(user);

            IndividualInvestorEntity savedIndividualInvestor = individualInvestorRepo.save(individualInvestor);

            List<IndividualInvestorEntity> userIndividualInvestors = user.getIndividualInvestors();
            userIndividualInvestors.add(individualInvestor);  // Associate the startup with the user
            user.setIndividualInvestors(userIndividualInvestors);

            userRepo.save(user);

            response.setSuccess(true);
            response.setMessage("investor created successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e){
            response.setSuccess(false);
            response.setMessage("investor creation failed");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Return the image belonging to a specific individual investor
    @GetMapping("/individual_investor/{individualinvestorId}/image")
    public ResponseEntity<?> retrieveIndividualInvestorImage(@PathVariable Integer individualinvestorId) throws IOException{
        byte[] imageData = imageService.retrieveIndividualInvestorImage(individualinvestorId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
}
