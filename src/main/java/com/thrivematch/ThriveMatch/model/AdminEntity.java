package com.thrivematch.ThriveMatch.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "admin")
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;

    @OneToMany(mappedBy = "admin")
    private List<TokenEntity> tokens;

    @OneToMany(mappedBy = "admin")
    private List<StartUpEntity> startups;

    @OneToMany(mappedBy = "admins")
    private List<InvestorEntity> investors;

    @OneToMany(mappedBy = "admin_individual_investor")
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

    public List<IndividualInvestorEntity> getIndividualInvestors() {
        return individualInvestors;
    }

    public void setIndividualInvestors(List<IndividualInvestorEntity> individualInvestors) {
        this.individualInvestors = individualInvestors;
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
}
