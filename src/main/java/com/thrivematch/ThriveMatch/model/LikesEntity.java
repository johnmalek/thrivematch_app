package com.thrivematch.ThriveMatch.model;

import jakarta.persistence.*;
import org.springframework.jmx.export.annotation.ManagedAttribute;

@Entity
public class LikesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private InvestorEntity investor;

    @ManyToOne
    private StartUpEntity startUp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InvestorEntity getInvestor() {
        return investor;
    }

    public void setInvestor(InvestorEntity investor) {
        this.investor = investor;
    }

    public StartUpEntity getStartUp() {
        return startUp;
    }

    public void setStartUp(StartUpEntity startUp) {
        this.startUp = startUp;
    }
}
