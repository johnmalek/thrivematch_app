package com.thrivematch.ThriveMatch.controller;

import com.thrivematch.ThriveMatch.dto.*;
import com.thrivematch.ThriveMatch.model.UserEntity;
import com.thrivematch.ThriveMatch.repository.AdminRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import com.thrivematch.ThriveMatch.security.CustomUserDetailsService;
import com.thrivematch.ThriveMatch.security.JwtGenerator;
import com.thrivematch.ThriveMatch.service.AdminService;
import com.thrivematch.ThriveMatch.service.StartUpService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.security.Principal;
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
    @Autowired
    private AdminService adminService;


    @PreAuthorize("hasRole('admin')")
    @PostMapping("/registerUser")
    public ResponseEntity<SuccessAndMessage> registerUser(@RequestBody UserRegister userRegisterDto, @RequestHeader(name="Authorization") String token) {
        return adminService.registerUser(userRegisterDto);
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<SuccessAndMessage> updateUser(@PathVariable Integer id, @RequestBody UserUpdate userUpdateDto, @RequestHeader(name="Authorization") String token) {
        System.out.println("userUpdate");
        return adminService.updateUser(id, userUpdateDto);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<SuccessAndMessage> deleteUser(@PathVariable Integer id, @RequestHeader(name="Authorization") String token) {
        System.out.println("deleteUser");
        return adminService.deleteUser(id);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/deleteAllUsers")
    public ResponseEntity<SuccessAndMessage> deleteAllUsers(@RequestHeader(name="Authorization") String token) {
        System.out.println("deleteAllUsers");
        return adminService.deleteAllUsers();
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/allUsers")
    public ResponseEntity<AllUserResponse> allUsers(@RequestHeader(name="Authorization") String token) {
        System.out.println("allUsers");
        return adminService.allUsers();
    }

    // Return a list of all investors
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/all_investors")
    public ResponseEntity<AdminInvestorInfoResponse> getAllInvestors(){
        return adminService.getAllInvestors();
    }

    // Return a list of all investors
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/all_startups")
    public ResponseEntity<AdminStartUpInfoResponse> getAllStartups(){
        return adminService.getAllStartups();
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/add_startup")
    public ResponseEntity<SuccessAndMessage> createStartUp(
            Principal principal,
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("address") String address,
            @RequestPart("poBox") String poBox,
            @RequestPart("year") String year,
            @RequestPart("image") MultipartFile file, @RequestHeader(name = "Authorization") String token) {
        return adminService.createStartUp(principal, name, email, description, industry, address, poBox, year, file);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/add_investor")
    public ResponseEntity<SuccessAndMessage> createInvestor(
            Principal principal,
            @RequestPart("name") String name,
            @RequestPart("email") String email,
            @RequestPart("desc") String description,
            @RequestPart("industry") String industry,
            @RequestPart("address") String address,
            @RequestPart("poBox") String poBox,
            @RequestPart("year") String year,
            @RequestPart("image") MultipartFile file, @RequestHeader(name = "Authorization") String token) {
        return adminService.createInvestor(principal, name, email, description, industry, address, poBox, year, file);
    }

    @PreAuthorize(("hasRole('admin')"))
    @PutMapping("/update_startup/{startupId}")
    public ResponseEntity<SuccessAndMessage> UpdateStartup(@RequestHeader(name = "Authorization") String token, AdminUpdateStartUp updateStartUp, @PathVariable Integer startupId){
        return adminService.UpdateStartUp(startupId, updateStartUp);
    }

    @PreAuthorize(("hasRole('admin')"))
    @PutMapping("/update_investor/{investorId}")
    public ResponseEntity<SuccessAndMessage> UpdateInvestor(@RequestHeader(name = "Authorization") String token, AdminUpdateStartUp updateStartUp, @PathVariable Integer investorId){
        return adminService.UpdateInvestor(investorId, updateStartUp);
    }

    @PreAuthorize(("hasRole('admin')"))
    @DeleteMapping("/delete_startup/{startUpId}")
    public ResponseEntity<SuccessAndMessage> deleteOneStartUp(@PathVariable Integer startUpId){
        return adminService.deleteOneStartUp(startUpId);
    }

    @PreAuthorize(("hasRole('admin')"))
    @DeleteMapping("/delete_investor/{investorId}")
    public ResponseEntity<SuccessAndMessage> deleteOneInvestor(@PathVariable Integer investorId){
        return adminService.deleteOneInvestor(investorId);
    }

}
