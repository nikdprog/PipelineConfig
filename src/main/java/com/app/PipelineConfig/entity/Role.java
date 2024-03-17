package com.app.PipelineConfig.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_of_user")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Role() {}

    public Role(String name) {
        super();
        this.name= name;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name= name;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
