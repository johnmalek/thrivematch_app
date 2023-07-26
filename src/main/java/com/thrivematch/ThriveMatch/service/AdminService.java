package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.dto.*;
import com.thrivematch.ThriveMatch.model.*;
import com.thrivematch.ThriveMatch.repository.*;
import com.thrivematch.ThriveMatch.security.CustomUserDetailsService;
import com.thrivematch.ThriveMatch.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private TokenRepo tokenRepo;
    @Autowired
    private StartUpRepo startUpRepo;
    @Autowired
    private InvestorRepo investorRepo;
    @Autowired
    private ImageService imageService;
    @Autowired
    private IndividualInvestorRepo individualInvestorRepo;


    // Register a user
    public ResponseEntity<SuccessAndMessage> registerUser(UserRegister userRegisterDto) {
        SuccessAndMessage response = new SuccessAndMessage();
        if(userRepo.existsByEmail(userRegisterDto.getEmail())) {
            response.setMessage("Email is already registered !!");
            response.setSuccess(false);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegisterDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        userEntity.setEmail(userRegisterDto.getEmail());
        userRepo.save(userEntity);
        response.setMessage("User Created Successfully !!");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

    public ResponseEntity<SuccessAndMessage> updateUser(Integer id, UserUpdate userUpdateDto) {
        System.out.println("userUpdate");
        SuccessAndMessage response = new SuccessAndMessage();
        if(!(userRepo.existsById(id))) {
            response.setMessage("User does not exist");
            response.setSuccess(false);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<UserEntity> user = userRepo.findById(id);
        UserEntity userEntity = user.get();
        userEntity.setUsername(userUpdateDto.getUsername());
        userEntity.setEmail(userUpdateDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        userRepo.save(userEntity);
        response.setMessage("User updated successfully");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

    // Delete a user
    public ResponseEntity<SuccessAndMessage> deleteUser(Integer id) {
        SuccessAndMessage response = new SuccessAndMessage();
        if(!(userRepo.existsById(id))) {
            response.setMessage("User does not exist");
            response.setSuccess(false);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.BAD_REQUEST);
        }
        userRepo.deleteById(id);
        response.setMessage("User deleted successfully");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

    // Delete all users
    public ResponseEntity<SuccessAndMessage> deleteAllUsers() {
        System.out.println("deleteAllUsers");
        SuccessAndMessage response = new SuccessAndMessage();
        userRepo.deleteAll();
        response.setMessage("Users deleted successfully");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

    // List all users
    public ResponseEntity<AllUserResponse> allUsers(){
        ArrayList<UserEntity> users = new ArrayList<>(userRepo.findAll());
        AllUserResponse allUsersResponse = new AllUserResponse();
        ArrayList<UserDetailsDTO> userDetailDTOS = new ArrayList<>();
        if(users.size()>0){
            allUsersResponse.setMessage("all users found");
            allUsersResponse.setSuccess(true);
            UserDetailsDTO userDetail;
            for(UserEntity user: users){
                userDetail = new UserDetailsDTO();
                userDetail.setId(user.getId());
                userDetail.setEmail(user.getEmail());
                userDetail.setUsername(user.getUsername());
                userDetailDTOS.add(userDetail);
            }
            allUsersResponse.setUsers(userDetailDTOS);
            return ResponseEntity.ok().body(allUsersResponse);
        }
        allUsersResponse.setSuccess(false);
        allUsersResponse.setMessage("No User Found");
        return ResponseEntity.badRequest().body(allUsersResponse);
    }

//     Upload StartUp Information
    public ResponseEntity<SuccessAndMessage> createStartUp(
            Principal principal,
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("address") String address,
            @RequestPart("poBox") String poBox,
            @RequestPart("year") String year,
            @RequestPart("image" ) MultipartFile file) {
        SuccessAndMessage response = new SuccessAndMessage();

        String username = principal.getName();

        AdminEntity admin = adminRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin email " + username + " not found"));

        if (startUpRepo.existsByName(name)) {
            response.setSuccess(false);
            response.setMessage("Startup already exists");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String picture = imageService.uploadFile(file);
        StartUpEntity startUp = new StartUpEntity();
        startUp.setName(name);
        startUp.setEmail(email);
        startUp.setDescription(description);
        startUp.setIndustry(industry);
        startUp.setPoBox(poBox);
        startUp.setAddress(address);
        startUp.setYearFounded(LocalDate.parse(year));
        startUp.setPicturePath(picture);
        startUp.setDateCreated(LocalDate.now());
        startUp.setCreatedByAdmin(true);
        startUp.setAdmin(admin);

        admin.updateHasCreatedStartup();
        StartUpEntity savedStartUp = startUpRepo.save(startUp);

        List<StartUpEntity> adminStartups = admin.getStartups();
        adminStartups.add(startUp);  // Associate the startup with the user
        admin.setStartups(adminStartups);

        adminRepo.save(admin);

        response.setSuccess(true);
        response.setMessage("StartUp created successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//   Upload investor Information
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

        AdminEntity admin = adminRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin email " + username + " not found"));

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
        investor.setDateCreated(LocalDate.now());
        investor.setAdmins(admin);

        admin.updateHasCreatedInvestor();
        InvestorEntity savedStartUp = investorRepo.save(investor);

        List<InvestorEntity> adminInvestors = admin.getInvestors();
        adminInvestors.add(investor);  // Associate the startup with the user
        admin.setInvestors(adminInvestors);

        adminRepo.save(admin);

        response.setSuccess(true);
        response.setMessage("investor created successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Return a list of all investors in the db(including individual investors)
    public ResponseEntity<AdminInvestorInfoResponse> getAllInvestors(){
        ArrayList<InvestorEntity> investors = new ArrayList<>(investorRepo.findAll());
        ArrayList<IndividualInvestorEntity> individualInvestors = new ArrayList<>(individualInvestorRepo.findAll());
        AdminInvestorInfoResponse adminInvestorInfoResponse = new AdminInvestorInfoResponse();
        ArrayList<AdminInvestorDetails> investorDetails = new ArrayList<>();
        if(investors.size() > 0){
            adminInvestorInfoResponse.setSuccess(true);
            adminInvestorInfoResponse.setMessage("All investors");
            AdminInvestorDetails investorDetail;
            for(InvestorEntity investor: investors){
                investorDetail = new AdminInvestorDetails();
                investorDetail.setId(investor.getId());
                investorDetail.setName(investor.getName());
                investorDetail.setDateCreated(investor.getDateCreated());
                investorDetail.setLocation(investor.getAddress());
                investorDetail.setEmail(investor.getEmail());
                investorDetail.setIndustry(investor.getIndustry());
                investorDetails.add(investorDetail);
            }
            for(IndividualInvestorEntity individualInvestor: individualInvestors){
                investorDetail = new AdminInvestorDetails();
                investorDetail.setId(individualInvestor.getId());
                investorDetail.setName(individualInvestor.getName());
                investorDetail.setIndustry(individualInvestor.getIndustry());
                investorDetail.setEmail(individualInvestor.getEmail());
                investorDetail.setLocation(individualInvestor.getAddress());
                investorDetail.setDateCreated(individualInvestor.getDateCreated());
                investorDetails.add(investorDetail);
            }

            adminInvestorInfoResponse.setInvestors(investorDetails);
            return ResponseEntity.ok().body(adminInvestorInfoResponse);
        }
        adminInvestorInfoResponse.setSuccess(false);
        adminInvestorInfoResponse.setMessage("No investors found");
        return ResponseEntity.badRequest().body(adminInvestorInfoResponse);
    }

    // Return a list of all startups
    public ResponseEntity<AdminStartUpInfoResponse> getAllStartups(){
        ArrayList<StartUpEntity> startups = new ArrayList<>(startUpRepo.findAll());
        AdminStartUpInfoResponse startUpInfoResponse = new AdminStartUpInfoResponse();
        ArrayList<AdminStartUpDetails> adminStartUpDetails = new ArrayList<>();
        if(startups.size() > 0){
            startUpInfoResponse.setSuccess(true);
            startUpInfoResponse.setMessage("All startups");
            AdminStartUpDetails startUpDetail;
            for(StartUpEntity startUp: startups){
                startUpDetail = new AdminStartUpDetails();
                startUpDetail.setId(startUp.getId());
                startUpDetail.setName(startUp.getName());
                startUpDetail.setIndustry(startUp.getIndustry());
                startUpDetail.setAddress(startUp.getAddress());
                startUpDetail.setEmail(startUp.getEmail());
                startUpDetail.setDateCreated(startUp.getDateCreated());
                adminStartUpDetails.add(startUpDetail);
            }
            startUpInfoResponse.setStartups(adminStartUpDetails);
            return ResponseEntity.ok().body(startUpInfoResponse);
        }
        startUpInfoResponse.setSuccess(false);
        startUpInfoResponse.setMessage("No startups found");
        return ResponseEntity.badRequest().body(startUpInfoResponse);
    }

    // Update StartUp Info
    public ResponseEntity<SuccessAndMessage> UpdateStartUp(Integer startUpId, AdminUpdateStartUp updateStartUp){
        Optional<StartUpEntity> startUp = startUpRepo.findById(startUpId);
        SuccessAndMessage response = new SuccessAndMessage();
        if(startUp.isEmpty()){
            response.setSuccess(false);
            response.setMessage("Startup does not exist");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        String image = imageService.uploadFile(updateStartUp.getFile());
        StartUpEntity startup = startUp.get();
        startup.setName(updateStartUp.getName());
        startup.setDescription(updateStartUp.getDescription());
        startup.setEmail(updateStartUp.getEmail());
        startup.setIndustry(updateStartUp.getIndustry());
        startup.setAddress(updateStartUp.getAddress());
        startup.setPoBox(updateStartUp.getPoBox());
        startup.setPicturePath(image);

        startUpRepo.save(startup);
        response.setSuccess(true);
        response.setMessage("Startup update successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Update Investor Info
    public ResponseEntity<SuccessAndMessage> UpdateInvestor(Integer investorId, AdminUpdateStartUp updateStartUp){
        Optional<InvestorEntity> investor = investorRepo.findById(investorId);
        SuccessAndMessage response = new SuccessAndMessage();
        if(investor.isEmpty()){
            response.setSuccess(false);
            response.setMessage("Investor does not exist");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        String image = imageService.uploadFile(updateStartUp.getFile());
        InvestorEntity investor1 = investor.get();
        investor1.setName(updateStartUp.getName());
        investor1.setDescription(updateStartUp.getDescription());
        investor1.setEmail(updateStartUp.getEmail());
        investor1.setIndustry(updateStartUp.getIndustry());
        investor1.setAddress(updateStartUp.getAddress());
        investor1.setPoBox(updateStartUp.getPoBox());
        investor1.setPicturePath(image);

        investorRepo.save(investor1);
        response.setSuccess(true);
        response.setMessage("Investor update successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Delete one startup
    public ResponseEntity<SuccessAndMessage> deleteOneStartUp(Integer startUpId){
        SuccessAndMessage response = new SuccessAndMessage();
        if (startUpId == null) {
            response.setSuccess(false);
            response.setMessage("Startup ID must not be null");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if(!(startUpRepo.existsById(startUpId))){
            response.setSuccess(false);
            response.setMessage("StartUp does not exist");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        startUpRepo.deleteById(startUpId);
        response.setSuccess(true);
        response.setMessage("StartUp deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Delete one Investor
    public ResponseEntity<SuccessAndMessage> deleteOneInvestor(Integer investorId){
        SuccessAndMessage response = new SuccessAndMessage();
        if (investorId == null) {
            response.setSuccess(false);
            response.setMessage("Investor ID must not be null");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if(!(investorRepo.existsById(investorId))){
            response.setSuccess(false);
            response.setMessage("Investor does not exist");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        startUpRepo.deleteById(investorId);
        response.setSuccess(true);
        response.setMessage("Investor deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
