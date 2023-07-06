package com.thrivematch.ThriveMatch.model;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "field cannot be null")
    private String username;
    @Email(message = "invalid email address")
    private String email;
    private boolean status;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\W)(?=\\\\S+$).{8,35}$",
            message = "password must have one uppercase character, one lowercase character, one special character and must be " +
                    "8-35 characters long")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<TokenEntity> tokens;

}
