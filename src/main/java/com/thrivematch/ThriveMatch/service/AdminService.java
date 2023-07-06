package com.thrivematch.ThriveMatch.service;

import com.thrivematch.ThriveMatch.dto.*;
import com.thrivematch.ThriveMatch.model.AdminEntity;
import com.thrivematch.ThriveMatch.model.UserEntity;
import com.thrivematch.ThriveMatch.model.UserType;
import com.thrivematch.ThriveMatch.repository.AdminRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import com.thrivematch.ThriveMatch.security.CustomUserDetailsService;
import com.thrivematch.ThriveMatch.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
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
        userEntity.setStatus(true);
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
        userEntity.setStatus(true);
        userRepo.save(userEntity);
        response.setMessage("User updated successfully");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

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

    public ResponseEntity<SuccessAndMessage> deleteAllUsers() {
        System.out.println("deleteAllUsers");
        SuccessAndMessage response = new SuccessAndMessage();
        userRepo.deleteAll();
        response.setMessage("Users deleted successfully");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

    public ResponseEntity<AllUserResponse> allUsers(){
        ArrayList<UserEntity> users = new ArrayList<>(userRepo.findAll());
        AllUserResponse allUsersResponse = new AllUserResponse();
        ArrayList<UserDetails> userDetails = new ArrayList<>();
        if(users.size()>0){
            allUsersResponse.setMessage("all users found");
            allUsersResponse.setSuccess(true);
            UserDetails userDetail;
            for(UserEntity user: users){
                userDetail = new UserDetails();
                userDetail.setId(user.getId());
                userDetail.setEmail(user.getEmail());
                userDetail.setUsername(user.getUsername());
                userDetails.add(userDetail);
            }
            allUsersResponse.setUsers(userDetails);
            return ResponseEntity.ok().body(allUsersResponse);
        }
        allUsersResponse.setSuccess(false);
        allUsersResponse.setMessage("No User Found");
        return ResponseEntity.badRequest().body(allUsersResponse);
    }
}
