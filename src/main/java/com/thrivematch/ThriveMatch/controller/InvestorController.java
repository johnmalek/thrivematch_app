package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.InvestorInfoResponse;
import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.model.LikesEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.repository.InvestorRepo;
import com.thrivematch.ThriveMatch.service.ImageService;
import com.thrivematch.ThriveMatch.service.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class InvestorController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private InvestorRepo investorRepo;
    @Autowired
    private InvestorService investorService;


    // Upload investor Information
    @PostMapping("/investors")
    public ResponseEntity<SuccessAndMessage> createInvestor(
            Principal principal,
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("address") String address,
            @RequestPart("poBox") String poBox,
            @RequestPart("year") String year,
            @RequestPart("image" ) MultipartFile file, @RequestHeader(name="Authorization") String token){
        return investorService.createInvestor(principal ,name, email, description, industry, address, poBox, year, file);
    }

    // Return a list of all investors
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    @GetMapping("all_investors")
    public ResponseEntity<InvestorInfoResponse> getAllInvestors(){
        return investorService.getAllInvestors();
    }

    // Return the image belonging to a specific investor
    @GetMapping("/investor/{investorId}/image")
    public ResponseEntity<?> downloadInvestorImage(@PathVariable Integer investorId) throws IOException{
        byte[] imageData = imageService.retrieveInvestorImage(investorId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    //Return a list of investors that have liked the startup
    @GetMapping("/investors/{investorId}/likes")
    public ResponseEntity<?> likes(@PathVariable Integer investorId){
        return investorService.likes(investorId);
    }
}
