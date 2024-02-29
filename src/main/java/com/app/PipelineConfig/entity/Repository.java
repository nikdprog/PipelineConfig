package com.app.PipelineConfig.entity;

public class Repository {

    private String name;
    private String url;
    private String language_of_programming;

    public Repository(String name, String url, String language_of_programming) {
        this.name = name;
        this.url = url;
        this.language_of_programming = language_of_programming;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
    public String getLanguage_of_programming() { return language_of_programming; }
    public void setName() { this.name = name; }
    public void setUrl() { this.url = url; }
    public void setLanguage_of_programming() { this.language_of_programming = language_of_programming; }


}
