package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.IndividualInvestorEntity;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.model.UserEntity;
import com.thrivematch.ThriveMatch.repository.IndividualInvestorRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import com.thrivematch.ThriveMatch.service.ImageService;
import com.thrivematch.ThriveMatch.service.IndividualInvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Autowired
    private IndividualInvestorService individualInvestorService;


    // Upload investor information
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    @PostMapping("/individual_investor")
    public ResponseEntity<SuccessAndMessage> createProfile(
            Principal principal,
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("address") String address,
            @RequestPart("image" ) MultipartFile file, @RequestHeader(name="Authorization") String token){
        SuccessAndMessage response = new SuccessAndMessage();
        return individualInvestorService.createProfile(principal, name, email, description, industry, address, file);
    }

    // Return the image belonging to a specific individual investor
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    @GetMapping("/individual_investor/{individualinvestorId}/image")
    public ResponseEntity<?> retrieveIndividualInvestorImage(@PathVariable Integer individualinvestorId) throws IOException{
        String imageData = imageService.retrieveIndividualInvestorImage(individualinvestorId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(imageData);
    }
}
