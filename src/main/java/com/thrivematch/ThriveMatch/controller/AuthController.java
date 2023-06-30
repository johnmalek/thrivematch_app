package com.thrivematch.ThriveMatch.controller;

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
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping("api/v1/adminRegister")
    public ResponseEntity<String> adminRegister(@RequestBody AdminAuth adminAuthDto) {
        if(adminRepo.existsByUsername(adminAuthDto.getUsername())) {
            return new ResponseEntity<String>("Username is taken !! ",HttpStatus.BAD_REQUEST);
        }
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setUsername(adminAuthDto.getUsername());
        adminEntity.setPassword(passwordEncoder.encode(adminAuthDto.getPassword()));

        adminRepo.save(adminEntity);
        return new ResponseEntity<String>("Admin Registration successful !! ", HttpStatus.CREATED);
    }

    @PostMapping("api/v1/adminLogin")
    public ResponseEntity<AdminLoginResponse> adminLogin(@RequestBody AdminAuth adminAuthDto) {
        System.out.println("adminLogin");
        customUserDetailsService.setUserType(UserType.ADMIN);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminAuthDto.getUsername(), adminAuthDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication,UserType.ADMIN.toString());
        AdminLoginResponse responseDto = new AdminLoginResponse();
        AdminEntity adminEntity = adminRepo.findByUsername(adminAuthDto.getUsername()).orElseThrow();
        if(!passwordEncoder.matches(adminAuthDto.getPassword(), adminEntity.getPassword())){
            responseDto.setSuccess(false);
            responseDto.setMessage("incorrect username or password");
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        responseDto.setSuccess(true);
        responseDto.setMessage("login successful !!");
        responseDto.setToken(token);
        responseDto.setAdmin(adminEntity.getUsername(), adminEntity.getId());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("api/v1/userRegister")
    public ResponseEntity<SuccessAndMessage> userRegister(@RequestBody UserRegister studentRegisterDto) {
        System.out.println("userRegister");
        SuccessAndMessage response = new SuccessAndMessage();
        if(userRepo.existsByEmail(studentRegisterDto.getEmail())) {
            response.setMessage("Email is already registered !!");
            response.setSuccess(false);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setName(studentRegisterDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(studentRegisterDto.getPassword()));
        userEntity.setEmail(studentRegisterDto.getEmail());
        userEntity.setStatus(true);
        userRepo.save(userEntity);
        response.setMessage("User Created Successfully !!");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

    @PostMapping("api/v1/userLogin")
    public ResponseEntity<UserLoginResponse> userLogin(@RequestBody UserLogin userLoginDto) {
        System.out.println("userLogin");
        customUserDetailsService.setUserType(UserType.USER);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication, UserType.USER.toString());
        UserLoginResponse responseDto = new UserLoginResponse();
        UserEntity userEntity = userRepo.findByEmail(userLoginDto.getEmail()).orElseThrow();

        if(!passwordEncoder.matches(userLoginDto.getPassword(), userEntity.getPassword())){
            responseDto.setSuccess(false);
            responseDto.setMessage("incorrect username or password");
            return new ResponseEntity<UserLoginResponse>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        responseDto.setSuccess(true);
        responseDto.setMessage("login successful !!");
        responseDto.setToken(token);
        responseDto.setUser(userEntity.getName(), userEntity.getEmail(), userEntity.getId());
        return new ResponseEntity<UserLoginResponse>(responseDto, HttpStatus.OK);
    }

    @PutMapping("api/v1/updateUser/{id}")
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



}
