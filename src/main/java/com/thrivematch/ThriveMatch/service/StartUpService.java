package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.dto.StartUpDetails;
import com.thrivematch.ThriveMatch.dto.StartUpInfoResponse;
import com.thrivematch.ThriveMatch.dto.SuccessAndMessage;
import com.thrivematch.ThriveMatch.dto.UserDetailsDTO;
import com.thrivematch.ThriveMatch.model.DocumentsEntity;
import com.thrivematch.ThriveMatch.model.LikesEntity;
import com.thrivematch.ThriveMatch.model.StartUpEntity;
import com.thrivematch.ThriveMatch.model.UserEntity;
import com.thrivematch.ThriveMatch.repository.DocumentsRepo;
import com.thrivematch.ThriveMatch.repository.StartUpRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StartUpService {
    @Autowired
    private StartUpRepo startUpRepo;
    @Autowired
    private ImageService imageService;
    @Autowired
    private DocumentsRepo documentsRepo;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private UserRepo userRepo;

    // Upload StartUp Information
    public ResponseEntity<SuccessAndMessage> createStartUp(
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("address") String address,
            @RequestPart("poBox") String poBox,
            @RequestPart("year") String year,
            @RequestPart("image" ) MultipartFile file){
        SuccessAndMessage response = new SuccessAndMessage();

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() instanceof UserDetails userDetails){
                // Retrieve authenticated user details
                String username = userDetails.getUsername();

                // Find the user entity based on username
                UserEntity user = userRepo.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User email "+ username + " not found"));

                if(startUpRepo.existsById(user.getId())){
                    response.setSuccess(false);
                    response.setMessage("Startup already exists");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }

                byte[] image = imageService.uploadStartUpImage(file).getImage();
                StartUpEntity startUp = new StartUpEntity();
                startUp.setName(name);
                startUp.setEmail(email);
                startUp.setDescription(description);
                startUp.setIndustry(industry);
                startUp.setPoBox(poBox);
                startUp.setAddress(address);
                startUp.setYearFounded(LocalDate.parse(year));
                startUp.setImage(image);
                startUp.setUser(user);

                StartUpEntity savedStartUp = startUpRepo.save(startUp);

                List<StartUpEntity> userStartups = user.getStartups();
                userStartups.add(startUp);  // Associate the startup with the user
                user.setStartups(userStartups);

                userRepo.save(user);

                response.setSuccess(true);
                response.setMessage("startUp created successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else{
                response.setSuccess(false);
                response.setMessage("StartUp creation failed");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e){
            response.setSuccess(false);
            response.setMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Upload StartUp Docs
    public ResponseEntity<SuccessAndMessage> uploadDocs(@PathVariable Integer startupId, @RequestPart("file") MultipartFile file) throws IOException {
        SuccessAndMessage response = new SuccessAndMessage();
        try {
            if (documentsRepo.existsByFilePath(file.getOriginalFilename())) {
                response.setSuccess(false);
                response.setMessage("File exists");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            StartUpEntity startup = startUpRepo.findById(startupId).orElseThrow();
            String filePath = documentService.saveFile(file);
            DocumentsEntity documentsEntity = new DocumentsEntity();
            documentsEntity.setFilePath(filePath);
            documentsEntity.setStartup(startup);

            documentsRepo.save(documentsEntity);
            response.setSuccess(true);
            response.setMessage("File uploaded successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            response.setSuccess(false);
            response.setMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Return a list of all startups
    public ResponseEntity<StartUpInfoResponse> getAllStartups(){
        ArrayList<StartUpEntity> startups = new ArrayList<>(startUpRepo.findAll());
        StartUpInfoResponse startUpInfoResponse = new StartUpInfoResponse();
        ArrayList<StartUpDetails> startUpDetails = new ArrayList<>();
        if(startups.size() > 0){
            startUpInfoResponse.setSuccess(true);
            startUpInfoResponse.setMessage("All startups");
            StartUpDetails startUpDetail;
            for(StartUpEntity startUp: startups){
                startUpDetail = new StartUpDetails();
                startUpDetail.setName(startUp.getName());
                startUpDetail.setIndustry(startUp.getIndustry());
                startUpDetail.setDescription(startUp.getDescription());
                startUpDetail.setImage(startUp.getImage());
                startUpDetails.add(startUpDetail);
            }
            startUpInfoResponse.setStartups(startUpDetails);
            return ResponseEntity.ok().body(startUpInfoResponse);
        }
        startUpInfoResponse.setSuccess(false);
        startUpInfoResponse.setMessage("No startups found");
        return ResponseEntity.badRequest().body(startUpInfoResponse);
    }

    //Download a file belonging to a specific startup
    public ResponseEntity<?> downloadFile(@PathVariable Integer startupId, @PathVariable Integer fileId) throws IOException{
        StartUpEntity startup = startUpRepo.findById(startupId).orElse(null);
        if (startup == null) {
            // Handle startup not found error
            return ResponseEntity.notFound().build();
        }

        // Check if the startup has any documents
        List<DocumentsEntity> documents = startup.getDocuments();
        if (documents == null || documents.isEmpty()) {
            // Handle no documents found error
            return ResponseEntity.notFound().build();
        }

        DocumentsEntity document = documentsRepo.findById(fileId).orElseThrow();
        if (document == null || !documents.contains(document)) {
            // Handle document not found or not associated with the startup error
            return ResponseEntity.notFound().build();
        }

        byte[] fileData = documentService.downloadFile(fileId);

        // Determine the content type based on the file extension
        String contentType;
        String filename = document.getFilePath();
        if (filename.endsWith(".png")) {
            contentType = "image/png";
        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (filename.endsWith(".pdf")) {
            contentType = "application/pdf";
        } else if (filename.endsWith(".docx")) {
            contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(fileData);
    }

    //Return a list of files belonging to a specific startup
    public ResponseEntity<?> allFiles(@PathVariable Integer startupId){
        Optional<StartUpEntity> startUp = startUpRepo.findById(startupId);
        if (startUp.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<DocumentsEntity> files = startUp.get().getDocuments();
        return ResponseEntity.ok().body(files);
    }

    // Startup delete one file
    public ResponseEntity<SuccessAndMessage> deleteOneFile(@PathVariable Integer startupId, @PathVariable Integer fileId) throws IOException{
        StartUpEntity startup = startUpRepo.findById(startupId).orElse(null);
        SuccessAndMessage response = new SuccessAndMessage();
        // Handle startup not found error
        if (startup == null) {
            response.setSuccess(false);
            response.setMessage("Startup with ID "+ startupId + " not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        DocumentsEntity document = documentsRepo.findById(fileId).orElseThrow();
        // Handle a case where the file does not exist for the startup
        if (document == null){
            response.setSuccess(false);
            response.setMessage("File not found");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }else{
            documentsRepo.delete(document);
            response.setSuccess(true);
            response.setMessage("File deleted");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    // Startup delete all files
    public ResponseEntity<SuccessAndMessage> deleteAllFiles(@PathVariable Integer startupId) throws IOException{
        SuccessAndMessage response = new SuccessAndMessage();
        StartUpEntity startup = startUpRepo.findById(startupId).orElse(null);
        // Handle startup not found error
        if (startup == null) {
            response.setSuccess(false);
            response.setMessage("StartUp with ID "+ startupId + " not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        List<DocumentsEntity> docsToDelete = startup.getDocuments();
        // Handle a case where there are no files for the startup
        if(docsToDelete == null || docsToDelete.isEmpty()){
            response.setSuccess(false);
            response.setMessage("No files found for the startup");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        for (DocumentsEntity doc : docsToDelete) {
            doc.setStartup(null); // Remove the association with the startup
        }
        startup.getDocuments().clear();
        documentsRepo.deleteAll(docsToDelete);
        response.setSuccess(true);
        response.setMessage("Files deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Return a list of investors that have liked the startup
    public ResponseEntity<?> likes(@PathVariable Integer startupId){
        Optional<StartUpEntity> startup = startUpRepo.findById(startupId);

        if(startup.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<LikesEntity> likedInvestors = startup.get().getLikes();
        return ResponseEntity.ok(likedInvestors);
    }

}
