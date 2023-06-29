package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.*;
import com.thrivematch.ThriveMatch.model.UserEntity;
import com.thrivematch.ThriveMatch.repository.AdminRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import com.thrivematch.ThriveMatch.security.CustomUserDetailsService;
import com.thrivematch.ThriveMatch.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/registerUser")
    public ResponseEntity<SuccessAndMessage> registerUser(@RequestBody UserRegister teacherRegisterDto, @RequestHeader(name="Authorization") String token) {
        SuccessAndMessage response = new SuccessAndMessage();
        if(userRepo.existsByEmail(teacherRegisterDto.getEmail())) {
            response.setMessage("Email is already registered !!");
            response.setSuccess(false);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setName(teacherRegisterDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(teacherRegisterDto.getPassword()));
        userEntity.setEmail(teacherRegisterDto.getEmail());
        userEntity.setStatus(true);
        userRepo.save(userEntity);
        response.setMessage("User Created Successfully !!");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<SuccessAndMessage> updateUser(@PathVariable Integer id, @RequestBody UserUpdate userUpdateDto, @RequestHeader(name="Authorization") String token) {
        System.out.println("userUpdate");
        SuccessAndMessage response = new SuccessAndMessage();
        if(!(userRepo.existsById(id))) {
            response.setMessage("User does not exist");
            response.setSuccess(false);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<UserEntity> user = userRepo.findById(id);
        UserEntity userEntity = user.get();
        userEntity.setName(userUpdateDto.getName());
        userEntity.setEmail(userUpdateDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        userEntity.setStatus(true);
        userRepo.save(userEntity);
        response.setMessage("User updated successfully");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<SuccessAndMessage> deleteUser(@PathVariable Integer id, @RequestHeader(name="Authorization") String token) {
        System.out.println("deleteUser");
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

    @DeleteMapping("/deleteAllUsers")
    public ResponseEntity<SuccessAndMessage> deleteAllUsers(@RequestHeader(name="Authorization") String token) {
        System.out.println("deleteAllUsers");
        SuccessAndMessage response = new SuccessAndMessage();
        userRepo.deleteAll();
        response.setMessage("Users deleted successfully");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<ArrayList<AllUsersResponse>> allUsers(@RequestHeader(name="Authorization") String token){
        ArrayList<UserEntity> users = new ArrayList<>(userRepo.findAll());
        ArrayList<AllUsersResponse> responseDTOs = new ArrayList<>();
        AllUsersResponse allUsersResponse = new AllUsersResponse();
      if(users.size()>0){
          for(int i = 0; i<users.size();i++){
              UserEntity user = users.get(i);
              allUsersResponse.setUser(user.getName(),user.getEmail(), user.getId());
              allUsersResponse.setSuccess(true);
              allUsersResponse.setMessage("All Users");
              responseDTOs.add(allUsersResponse);
          }
          return ResponseEntity.ok().body(responseDTOs);
      }
      allUsersResponse.setSuccess(false);
      allUsersResponse.setMessage("No User Found");
      responseDTOs.add(allUsersResponse);
      return ResponseEntity.badRequest().body(responseDTOs);
    }

}
