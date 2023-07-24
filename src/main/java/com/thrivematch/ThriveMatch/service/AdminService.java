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
        startUp.setAdmin(admin);

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
        investor.setAdmins(admin);

        InvestorEntity savedStartUp = investorRepo.save(investor);

        List<InvestorEntity> adminInvestors = admin.getInvestors();
        adminInvestors.add(investor);  // Associate the startup with the user
        admin.setInvestors(adminInvestors);

        adminRepo.save(admin);

        response.setSuccess(true);
        response.setMessage("investor created successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
