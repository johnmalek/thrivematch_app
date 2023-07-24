package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.dto.*;
import com.thrivematch.ThriveMatch.dto.InvestorDetails;
import com.thrivematch.ThriveMatch.dto.InvestorInfoResponse;
import com.thrivematch.ThriveMatch.model.*;
import com.thrivematch.ThriveMatch.model.InvestorEntity;
import com.thrivematch.ThriveMatch.repository.IndividualInvestorRepo;
import com.thrivematch.ThriveMatch.repository.InvestorRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvestorService {
    @Autowired
    private InvestorRepo investorRepo;
    @Autowired
    private IndividualInvestorRepo individualInvestorRepo;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserRepo userRepo;


    // Upload investor Information
    public ResponseEntity<SuccessAndMessage> createInvestor(
            Principal principal,
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("address") String address,
            @RequestPart("poBox") String poBox,
            @RequestPart("year") String year,
            @RequestPart("image" ) MultipartFile file){
        SuccessAndMessage response = new SuccessAndMessage();

        String username = principal.getName();

        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User email " + username + " not found"));

        if (investorRepo.existsByName(name)) {
            response.setSuccess(false);
            response.setMessage("investor already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        String picture = imageService.uploadFile(file);
        InvestorEntity investor = new InvestorEntity();
        investor.setName(name);
        investor.setEmail(email);
        investor.setDescription(description);
        investor.setIndustry(industry);
        investor.setPoBox(poBox);
        investor.setAddress(address);
        investor.setYearFounded(LocalDate.parse(year));
        investor.setPicturePath(picture);
        investor.setUser(user);

        InvestorEntity savedStartUp = investorRepo.save(investor);

        List<InvestorEntity> userInvestors = user.getInvestors();
        userInvestors.add(investor);  // Associate the startup with the user
        user.setInvestors(userInvestors);

        userRepo.save(user);

        response.setSuccess(true);
        response.setMessage("investor created successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Return a list of all investors in the db(including individual investors)
    public ResponseEntity<InvestorInfoResponse> getAllInvestors(){
        ArrayList<InvestorEntity> investors = new ArrayList<>(investorRepo.findAll());
        ArrayList<IndividualInvestorEntity> individualInvestors = new ArrayList<>(individualInvestorRepo.findAll());
        InvestorInfoResponse investorInfoResponse = new InvestorInfoResponse();
        ArrayList<InvestorDetails> investorDetails = new ArrayList<>();
        if(investors.size() > 0){
            investorInfoResponse.setSuccess(true);
            investorInfoResponse.setMessage("All investors");
            InvestorDetails investorDetail;
            for(InvestorEntity investor: investors){
                investorDetail = new InvestorDetails();
                investorDetail.setId(investor.getId());
                investorDetail.setName(investor.getName());
                investorDetail.setIndustry(investor.getIndustry());
                investorDetail.setDescription(investor.getDescription());
                investorDetail.setPicturePath(investor.getPicturePath());
                investorDetails.add(investorDetail);
            }
            for(IndividualInvestorEntity individualInvestor: individualInvestors){
                investorDetail = new InvestorDetails();
                investorDetail.setId(investorDetail.getId());
                investorDetail.setName(individualInvestor.getName());
                investorDetail.setDescription(individualInvestor.getDescription());
                investorDetail.setIndustry(individualInvestor.getIndustry());
                investorDetail.setPicturePath(individualInvestor.getPicturePath());
                investorDetails.add(investorDetail);
            }

            investorInfoResponse.setInvestors(investorDetails);
            return ResponseEntity.ok().body(investorInfoResponse);
        }
        investorInfoResponse.setSuccess(false);
        investorInfoResponse.setMessage("No investors found");
        return ResponseEntity.badRequest().body(investorInfoResponse);
    }

    //Return a list of investors that have liked the startup
    public ResponseEntity<List<LikesDTO>> likes(@PathVariable Integer investorId){
        Optional<InvestorEntity> investor = investorRepo.findById(investorId);

        if(investor.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<LikesEntity> likedInvestors = new ArrayList<>(investor.get().getLikes());
        List<LikesDTO> likesDTOList = likedInvestors.stream()
                .map(likesEntity -> new LikesDTO(likesEntity.getInvestor().getName(), likesEntity.getInvestor().getPicturePath()))
                .collect(Collectors.toList());

        System.out.println(likesDTOList.size());

        return ResponseEntity.ok(likesDTOList);
    }
    
}
