package com.example.tuitioninfoapp.models;

import java.util.Date;
public class Material {
    private String id;
    private String courseId;
    private String title;
    private String fileUrl;
    private String type;

    // Firebase requires empty constructor
    public Material() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}