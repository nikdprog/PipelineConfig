package com.app.PipelineConfig.entity;


import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="pipelines")
public class Pipeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "repository_id")
    private RepositoryEntity repository;

    @Column(name="name")
    private String name;

    // метаданные - язык программирования (версия), jdk и система сборки, фреймворки тестирования и т.д.
    @Column(name="language_version")
    private String languageVersion;

    @Column(name="build_system")
    private String buildSystem;

    @Column(name="text")
    private String text;

    @Column(name="stages")
    private String[] stages;

    @Transient
    private Map<String, String> stageStatuses = new HashMap<>();

    @Column(name="status")
    private String status;

    public void setStageStatuses(Map<String,String> stageStatuses) {
        this.stageStatuses = stageStatuses;
    }
    public Map<String, String> getStageStatuses() {
        return stageStatuses;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStages(String[] stages) {
        this.stages = stages;
    }

    public String getBuildSystem() {
        return buildSystem;
    }
    public String[] getStages() {
        return stages;
    }
    //  статус??
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRepository(RepositoryEntity repositoryEntity) {
        this.repository = repositoryEntity;
    }

    public RepositoryEntity getRepository() {
        return repository;
    }
    public void setLanguageVersion(String languageVersion) {
        this.languageVersion = languageVersion;
    }

    public void setBuildSystem(String buildSystem) {
        this.buildSystem = buildSystem;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
