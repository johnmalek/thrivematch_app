package com.thrivematch.ThriveMatch.model;

import jakarta.persistence.*;

@Entity
public class DocumentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "startup_id")
    private StartUpEntity startup;

    public Integer getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StartUpEntity getStartup() {
        return startup;
    }

    public void setStartup(StartUpEntity startup) {
        this.startup = startup;
    }
}
