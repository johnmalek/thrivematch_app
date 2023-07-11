package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.StartUpInfoResponse;
import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.DocumentsEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.repository.DocumentsRepo;
import com.thrivematch.ThriveMatch.repository.StartUpRepo;
import com.thrivematch.ThriveMatch.service.DocumentService;
import com.thrivematch.ThriveMatch.service.FileService;
import com.thrivematch.ThriveMatch.service.StartUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class StartUpController {
    @Autowired
    private StartUpRepo startUpRepo;
    @Autowired
    private FileService fileService;
    @Autowired
    private StartUpService startUpService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentsRepo documentsRepo;

    @PostMapping("/startups")
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
        if(startUpRepo.existsByName(name)){
            response.setSuccess(false);
            response.setMessage("Startup with name "+ name + " already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            String picturePath = fileService.saveFile(file);
            StartUpEntity profile = new StartUpEntity();
            profile.setName(name);
            profile.setEmail(email);
            profile.setDescription(description);
            profile.setIndustry(industry);
            profile.setPoBox(poBox);
            profile.setAddress(address);
            profile.setYearFounded(LocalDate.parse(year));
            profile.setPicturePath(picturePath);
            StartUpEntity savedProfile = startUpRepo.save(profile);
            response.setSuccess(true);
            response.setMessage("Profile created successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e){
            response.setSuccess(false);
            response.setMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all_startups")
    public ResponseEntity<StartUpInfoResponse> getAllStartUps(@RequestHeader(name = "Authorization") String token){
        return startUpService.getAllStartups();
    }

    @GetMapping("/startup/{startupId}/image")
    public ResponseEntity<?> retrieveStartUpImage(@PathVariable Integer startupId) throws IOException{
        byte[] imageData = fileService.retrieveStartUpImage(startupId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @PostMapping("/startup/{startupId}/uploadDoc")
    public ResponseEntity<SuccessAndMessage> uploadDocs(@PathVariable Integer startupId, MultipartFile file) throws IOException {
        SuccessAndMessage response = new SuccessAndMessage();
        if(documentsRepo.existsByName(file)){
            response.setSuccess(false);
            response.setMessage("File exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            String filePath = fileService.saveFile(file);
            DocumentsEntity documentsEntity = new DocumentsEntity();
            documentsEntity.setFilePath(filePath);
            StartUpEntity startup = startUpRepo.findById(startupId).orElseThrow();
            List docs = new ArrayList();
            docs.add(filePath);
            startup.setDocuments(docs);
        } catch (IOException e){
            response.setSuccess(false);
            response.setMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
