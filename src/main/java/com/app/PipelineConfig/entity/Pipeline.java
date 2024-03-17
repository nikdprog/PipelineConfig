package com.app.PipelineConfig.entity;


import jakarta.persistence.*;

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
    //  статус??
}
