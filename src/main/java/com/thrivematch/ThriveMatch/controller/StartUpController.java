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

    //Have a specific startup upload a file
    @PostMapping("/startup/{startupId}/uploadDoc")
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

    //Download a file belonging to a specific startup
    @GetMapping("/startup/{startupId}/docs/{fileId}")
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

    @GetMapping("/startup/{startupId}/allFiles")
    public ResponseEntity<?> allFiles(@PathVariable Integer startupId){
        Optional<StartUpEntity> startUp = startUpRepo.findById(startupId);
        if (startUp.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<DocumentsEntity> files = startUp.get().getDocuments();
        return ResponseEntity.ok().body(files);
    }

    @DeleteMapping("/startup/{startupId}/deleteOneFile/{fileId}")
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

    @DeleteMapping("/startup/{startupId}/deleteAllFiles")
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
}
