package com.thrivematch.ThriveMatch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "startups")
public class StartUpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String industry;
    private String email;
    private String address;
    private String poBox;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate yearFounded;
    private String picturePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminEntity admin;

    @OneToMany(mappedBy = "startup")
    private List<LikesEntity> likes;

    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentsEntity> documents = new ArrayList<>();

    @ManyToMany(mappedBy = "startups", fetch = FetchType.LAZY)
    private Set<InvestorEntity> investors;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AdminEntity getAdmin() {
        return admin;
    }

    public void setAdmin(AdminEntity admin) {
        this.admin = admin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPoBox() {
        return poBox;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    public LocalDate getYearFounded() {
        return yearFounded;
    }

    public void setYearFounded(LocalDate yearFounded) {
        this.yearFounded = yearFounded;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<LikesEntity> getLikes() {
        return likes;
    }

    public void setLikes(List<LikesEntity> likes) {
        this.likes = likes;
    }

    public List<DocumentsEntity> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentsEntity> documents) {
        this.documents = documents;
    }

    public Set<InvestorEntity> getInvestors() {
        return investors;
    }

    public void setInvestors(Set<InvestorEntity> investors) {
        this.investors = investors;
    }
}
