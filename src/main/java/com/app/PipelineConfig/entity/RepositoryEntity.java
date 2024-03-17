package com.app.PipelineConfig.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user_repository")
public class RepositoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL)
    private List<Pipeline> pipelines;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "language")
    private String language;


    //private List<String> technologies;

    public RepositoryEntity() {}

    public RepositoryEntity(String name, String url, String language) {
        this.name = name;
        this.url = url;
        this.language = language;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
    public String getLanguage() { return language; }
    //public List<String> getTechnologies() { return technologies; }
    public void setName(String name) { this.name = name; }
    public void setUrl(String url) { this.url = url; }
    public void setLanguage(String language) { this.language = language; }
    //public void setTechnologies(List<String> technologies) { this.technologies = technologies; }
}
