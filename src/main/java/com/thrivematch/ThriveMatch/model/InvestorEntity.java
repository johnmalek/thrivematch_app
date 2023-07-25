package com.thrivematch.ThriveMatch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "investors")
public class InvestorEntity {
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
        @Column(name = "date_created")
        private LocalDate dateCreated;

        @OneToMany(mappedBy = "investor")
        private List<LikesEntity> likes;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private UserEntity user;

        @ManyToOne
        @JoinColumn(name = "admin_id")
        private AdminEntity admins;

        @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinTable(name = "investor_startups",
                joinColumns = {
                @JoinColumn(name = "investorId", referencedColumnName = "id")
            },
                inverseJoinColumns = {
                @JoinColumn(name = "startupId", referencedColumnName = "id")
                }
        )
        private Set<StartUpEntity> startups;

        private boolean createdByAdmin;

        public boolean isCreatedByAdmin() {
                return createdByAdmin;
        }

        public void setCreatedByAdmin(boolean createdByAdmin) {
                this.createdByAdmin = createdByAdmin;
        }

        @PostPersist
        public void updateOwnerHasCreatedInvestor() {
                this.createdByAdmin = false; // Assuming this investor is created by the user
                user.updateHasCreatedInvestor();
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public AdminEntity getAdmins() {
                return admins;
        }

        public void setAdmins(AdminEntity admins) {
                this.admins = admins;
        }

        public LocalDate getDateCreated() {
                return dateCreated;
        }

        public void setDateCreated(LocalDate dateCreated) {
                this.dateCreated = dateCreated;
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

        public List<LikesEntity> getLikes() {
                return likes;
        }

        public void setLikes(List<LikesEntity> likes) {
                this.likes = likes;
        }

        public UserEntity getUser() {
                return user;
        }

        public void setUser(UserEntity user) {
                this.user = user;
        }

        public Set<StartUpEntity> getStartups() {
                return startups;
        }

        public void setStartups(Set<StartUpEntity> startups) {
                this.startups = startups;
        }
}
