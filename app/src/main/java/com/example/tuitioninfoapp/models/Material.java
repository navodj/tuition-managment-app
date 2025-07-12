package com.example.tuitioninfoapp.models;

import java.util.Date;
public class Material {
    private String url;
    private String fileName;

    // Firebase requires empty constructor
    public Material() {}

    public Material(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    // Getters
    public String getUrl() { return url; }
    public String getFileName() { return fileName; }
}