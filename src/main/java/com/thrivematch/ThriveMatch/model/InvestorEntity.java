package com.thrivematch.ThriveMatch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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
//        @Lob
//        @Column(name = "image")
//        private byte[] image;

        @OneToMany(mappedBy = "investor")
        private List<LikesEntity> likes;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private UserEntity user;

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

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getName() {
                return name;
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

        public Set<StartUpEntity> getStartups() {
                return startups;
        }

        public void setStartups(Set<StartUpEntity> startups) {
                this.startups = startups;
        }
}
