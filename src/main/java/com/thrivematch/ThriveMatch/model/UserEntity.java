package com.thrivematch.ThriveMatch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    @Email
    private String email;
    private boolean status;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\W)(?=\\\\S+$).{8,35}$")
    private String password;

}
