package com.app.PipelineConfig.entity;

public class Repository {

    private String name;
    private String url;

    public Repository(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
    public void setName() { this.name = name; }
    public void setUrl() { this.url = url; }

}
