package com.thrivematch.ThriveMatch.security;

import com.thrivematch.ThriveMatch.model.AdminEntity;
import com.thrivematch.ThriveMatch.model.UserEntity;
import com.thrivematch.ThriveMatch.model.UserType;
import com.thrivematch.ThriveMatch.repository.AdminRepo;
import com.thrivematch.ThriveMatch.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private UserRepo userRepo;

    private UserType userType;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(userType);
        if(userType==UserType.ADMIN) {

            AdminEntity adminEntity = adminRepo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Admin Username "+ username+ "not found"));

            SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(UserType.ADMIN.toString());
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(adminAuthority);
            return new org.springframework.security.core.userdetails.User(adminEntity.getUsername(), adminEntity.getPassword(), authorities);
        } else if(userType == UserType.USER) {
            UserEntity userEntity = userRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User Email "+ username+ "not found"));

            SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(UserType.USER.toString());
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(adminAuthority);
            return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getPassword(), authorities);
        }
        return null;
    }

}