package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.*;
import com.thrivematch.ThriveMatch.model.AdminEntity;
import com.thrivematch.ThriveMatch.model.UserEntity;
import com.thrivematch.ThriveMatch.model.UserType;
import com.thrivematch.ThriveMatch.repository.AdminRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import com.thrivematch.ThriveMatch.security.CustomUserDetailsService;
import com.thrivematch.ThriveMatch.security.JwtGenerator;
//import com.thrivematch.ThriveMatch.service.LogOutService;
import com.thrivematch.ThriveMatch.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
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
    @Autowired
    private UserService userService;


    @PostMapping("api/v1/adminRegister")
    public ResponseEntity<String> adminRegister(@RequestBody AdminAuth adminAuthDto) {
        return userService.adminRegister(adminAuthDto);
    }

    @PostMapping("api/v1/adminLogin")
    public ResponseEntity<AdminLoginResponse> adminLogin(@RequestBody AdminAuth adminAuthDto) {
        return userService.adminLogin(adminAuthDto);
    }

    @PostMapping("api/v1/userRegister")
    public ResponseEntity<SuccessAndMessage> userRegister(@Valid @RequestBody UserRegister userRegisterDto) {
        System.out.println("userRegister");
        return userService.userRegister(userRegisterDto);
    }

    @PostMapping("api/v1/userLogin")
    public ResponseEntity<UserLoginResponse> userLogin(@RequestBody UserLogin userLoginDto) {
        System.out.println("userLogin");
        return userService.userLogin(userLoginDto);
    }

    @PutMapping("api/v1/updateUser/{id}")
    public ResponseEntity<SuccessAndMessage> updateUser(@PathVariable Integer id, @RequestBody UserUpdate userUpdateDto, @RequestHeader(name="Authorization") String token) {
        System.out.println("userUpdate");
        return userService.updateUser(id, userUpdateDto);
    }


}
