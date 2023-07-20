package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.IndividualInvestorEntity;
import com.thrivematch.ThriveMatch.model.UserEntity;
import com.thrivematch.ThriveMatch.repository.IndividualInvestorRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Service
public class IndividualInvestorService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private IndividualInvestorRepo individualInvestorRepo;
    @Autowired
    private ImageService imageService;

    public ResponseEntity<SuccessAndMessage> createProfile(
            Principal principal,
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("image" ) MultipartFile file){
        SuccessAndMessage response = new SuccessAndMessage();
        String username = principal.getName();

        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User email " + username + " not found"));

        if (individualInvestorRepo.existsByName(name)) {
            response.setSuccess(false);
            response.setMessage("investor already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        String picture = imageService.uploadFile(file);
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
    }
}
