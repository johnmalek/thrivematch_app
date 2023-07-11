//package com.thrivematch.ThriveMatch.model;
//
//import jakarta.persistence.*;
//
//import java.util.Optional;
//
//@Entity
//public class LikesEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ManyToOne
//    @JoinColumn(name = "investor")
//    private InvestorEntity investor;
//
//    @ManyToOne
//    @JoinColumn(name = "startup")
//    private StartUpEntity startUp;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public InvestorEntity getInvestor() {
//        return investor;
//    }
//
//    public void setInvestor(InvestorEntity investor) {
//        this.investor = investor;
//    }
//
//    public StartUpEntity getStartUp() {
//        return startUp;
//    }
//
//    public void setStartUp(StartUpEntity startUp) {
//        this.startUp = startUp;
//    }
//}
