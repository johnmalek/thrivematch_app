package com.thrivematch.ThriveMatch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "likes")
public class LikesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "investor_id")
    private InvestorEntity investor;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "startup_id")
    private StartUpEntity startup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public InvestorEntity getInvestor() {
        return investor;
    }

    public void setInvestor(InvestorEntity investor) {
        this.investor = investor;
    }

    public StartUpEntity getStartUp() {
        return startup;
    }

    public void setStartUp(StartUpEntity startup) {
        this.startup = startup;
    }
}
