package com.thrivematch.ThriveMatch.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "field cannot be null")
    private String username;
    @Email(message = "invalid email address")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\W)(?=\\\\S+$).{8,35}$",
            message = "password must have one uppercase character, one lowercase character, one special character and must be " +
                    "8-35 characters long")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<TokenEntity> tokens;

    @OneToMany(mappedBy = "user")
    private List<StartUpEntity> startups;

    @OneToMany(mappedBy = "user")
    private List<InvestorEntity> investors;

    @OneToMany(mappedBy = "user")
    private List<IndividualInvestorEntity> individualInvestors;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TokenEntity> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokenEntity> tokens) {
        this.tokens = tokens;
    }

    public List<StartUpEntity> getStartups() {
        return startups;
    }

    public void setStartups(List<StartUpEntity> startups) {
        this.startups = startups;
    }

    public List<InvestorEntity> getInvestors() {
        return investors;
    }

    public void setInvestors(List<InvestorEntity> investors) {
        this.investors = investors;
    }

    public List<IndividualInvestorEntity> getIndividualInvestors() {
        return individualInvestors;
    }

    public void setIndividualInvestors(List<IndividualInvestorEntity> individualInvestors) {
        this.individualInvestors = individualInvestors;
    }
}
