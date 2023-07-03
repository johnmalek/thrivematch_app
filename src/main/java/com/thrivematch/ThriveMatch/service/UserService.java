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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

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

    public ResponseEntity<String> adminRegister(AdminAuth adminAuthDto) {
        if(adminRepo.existsByUsername(adminAuthDto.getUsername())) {
            return new ResponseEntity<String>("Username is taken !! ", HttpStatus.BAD_REQUEST);
        }
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setUsername(adminAuthDto.getUsername());
        adminEntity.setPassword(passwordEncoder.encode(adminAuthDto.getPassword()));

        adminRepo.save(adminEntity);
        return new ResponseEntity<String>("Admin Registration successful !! ", HttpStatus.CREATED);
    }


    public ResponseEntity<AdminLoginResponse> adminLogin(AdminAuth adminAuthDto) {
        System.out.println("adminLogin");
        customUserDetailsService.setUserType(UserType.ADMIN);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminAuthDto.getUsername(), adminAuthDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication,UserType.ADMIN.toString());
        AdminLoginResponse responseDto = new AdminLoginResponse();
        AdminEntity adminEntity = adminRepo.findByUsername(adminAuthDto.getUsername()).orElseThrow();
        String encodedPassword = adminEntity.getPassword();
        String passedPassword = adminAuthDto.getPassword();
        boolean passwordsMatch = passwordEncoder.matches(passedPassword, encodedPassword);
        if(passwordsMatch){
            responseDto.setSuccess(true);
            responseDto.setMessage("login successful!");
            responseDto.setToken(token);
            responseDto.setAdmin(adminEntity.getUsername(), adminEntity.getId());
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        responseDto.setSuccess(false);
        responseDto.setMessage("Incorrect username or password");
        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<SuccessAndMessage> userRegister(UserRegister studentRegisterDto) {
        System.out.println("userRegister");
        SuccessAndMessage response = new SuccessAndMessage();
        if(userRepo.existsByEmail(studentRegisterDto.getEmail())) {
            response.setMessage("Email is already registered !!");
            response.setSuccess(false);
            return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(studentRegisterDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(studentRegisterDto.getPassword()));
        userEntity.setEmail(studentRegisterDto.getEmail());
        userEntity.setStatus(true);
        userRepo.save(userEntity);
        response.setMessage("User Created Successfully !!");
        response.setSuccess(true);
        return new ResponseEntity<SuccessAndMessage>(response, HttpStatus.OK);
    }

    public ResponseEntity<UserLoginResponse> userLogin(UserLogin userLoginDto) {
        System.out.println("userLogin");
        customUserDetailsService.setUserType(UserType.USER);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication, UserType.USER.toString());
        UserLoginResponse responseDto = new UserLoginResponse();
        UserEntity userEntity = userRepo.findByEmail(userLoginDto.getEmail()).orElseThrow();
        String encodedPassword = userEntity.getPassword();
        String passedPassword = userLoginDto.getPassword();
        boolean passwordsMatch = passwordEncoder.matches(passedPassword, encodedPassword);
        if(passwordsMatch){
            responseDto.setSuccess(true);
            responseDto.setMessage("login successful !!");
            responseDto.setToken(token);
            responseDto.setUser(userEntity.getUsername(), userEntity.getEmail(), userEntity.getId());
            return new ResponseEntity<UserLoginResponse>(responseDto, HttpStatus.OK);
        }
        responseDto.setSuccess(false);
        responseDto.setMessage("incorrect username or password");
        return new ResponseEntity<UserLoginResponse>(responseDto, HttpStatus.UNAUTHORIZED);
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

}
