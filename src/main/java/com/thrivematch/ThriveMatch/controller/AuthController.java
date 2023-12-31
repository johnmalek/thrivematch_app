package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.*;
import com.thrivematch.ThriveMatch.repository.AdminRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import com.thrivematch.ThriveMatch.security.CustomUserDetailsService;
import com.thrivematch.ThriveMatch.security.JwtGenerator;
//import com.thrivematch.ThriveMatch.service.LogOutService;
import com.thrivematch.ThriveMatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;


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

    @PreAuthorize("hasRole('user')")
    @PostMapping("api/v1/userRegister")
    public ResponseEntity<Response> userRegister(@Valid @RequestBody UserRegister userRegisterDto) {
        System.out.println("userRegister");
        return userService.userRegister(userRegisterDto);
    }

    @PreAuthorize("hasRole('user')")
    @PostMapping("api/v1/userLogin")
    public ResponseEntity<UserLoginResponse> userLogin(@RequestBody UserLogin userLoginDto) {
        System.out.println("userLogin");
        return userService.userLogin(userLoginDto);
    }

    @PreAuthorize("hasRole('user')")
    @PutMapping("api/v1/updateUser/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Integer id, @RequestBody UserUpdate userUpdateDto, @RequestHeader(name="Authorization") String token) {
        System.out.println("userUpdate");
        return userService.updateUser(id, userUpdateDto);
    }


}
