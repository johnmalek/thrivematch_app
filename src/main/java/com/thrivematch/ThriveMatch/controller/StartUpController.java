package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.StartUpInfoResponse;
import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.model.DocumentsEntity;
import com.thrivematch.ThriveMatch.model.LikesEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.model.UserEntity;
import com.thrivematch.ThriveMatch.repository.DocumentsRepo;
import com.thrivematch.ThriveMatch.repository.StartUpRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import com.thrivematch.ThriveMatch.service.DocumentService;
import com.thrivematch.ThriveMatch.service.ImageService;
import com.thrivematch.ThriveMatch.service.StartUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class StartUpController {
    @Autowired
    private StartUpRepo startUpRepo;
    @Autowired
    private ImageService imageService;
    @Autowired
    private StartUpService startUpService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentsRepo documentsRepo;
    @Autowired
    private UserRepo userRepo;

    //Upload startup information
    @PreAuthorize("hasRole('user')")
    @PostMapping("/add_startup")
    public ResponseEntity<SuccessAndMessage> createStartUp(
            Principal principal,
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("address") String address,
            @RequestPart("poBox") String poBox,
            @RequestPart("year") String year,
            @RequestPart("image") MultipartFile file, @RequestHeader(name = "Authorization") String token) {
        return startUpService.createStartUp(principal, name, email, description, industry, address, poBox, year, file);
    }

    // Return a list of all startups
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    @GetMapping("/all_startups")
    public ResponseEntity<StartUpInfoResponse> getAllStartUps(@RequestHeader(name = "Authorization") String token) {
        return startUpService.getAllStartups();
    }

    // Return the image belonging to a specific startup
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    @GetMapping("/startup/{startupId}/image")
    public ResponseEntity<String> retrieveStartUpImage(@PathVariable Integer startupId) throws IOException {
        String imageData = imageService.retrieveStartUpImage(startupId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(imageData);
    }

    //Have a specific startup upload a file
    @PostMapping("/startup/{startupId}/uploadDoc")
    public ResponseEntity<SuccessAndMessage> uploadDocs(@PathVariable Integer startupId, @RequestPart("file") MultipartFile file) throws IOException {
        return startUpService.uploadDocs(startupId, file);
    }

    //Download a file belonging to a specific startup
//    @GetMapping("/startup/{startupId}/docs/{fileId}")
//    public ResponseEntity<?> downloadFile(@PathVariable Integer startupId, @PathVariable Integer fileId) throws IOException{
//        return startUpService.downloadFile(startupId, fileId);
//    }

    //Return a list of files belonging to a specific startup
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    @GetMapping("/startup/{startupId}/allFiles")
    public ResponseEntity<?> allFiles(@PathVariable Integer startupId){
        return startUpService.allFiles(startupId);
    }

    // Startup delete one file
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    @DeleteMapping("/startup/{startupId}/deleteOneFile/{fileId}")
    public ResponseEntity<SuccessAndMessage> deleteOneFile(@PathVariable Integer startupId, @PathVariable Integer fileId) throws IOException{
        return startUpService.deleteOneFile(startupId, fileId);
    }

    // Startup delete all files
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    @DeleteMapping("/startup/{startupId}/deleteAllFiles")
    public ResponseEntity<SuccessAndMessage> deleteAllFiles(@PathVariable Integer startupId) throws IOException{
        return startUpService.deleteAllFiles(startupId);
    }

     //Return a list of investors that have liked the startup
     @PreAuthorize("hasRole('admin') or hasRole('user')")
    @GetMapping("/startups/{startupId}/likes")
    public ResponseEntity<?> likes(@PathVariable Integer startupId){
        return startUpService.likes(startupId);
    }
}
