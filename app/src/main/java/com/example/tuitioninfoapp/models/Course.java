package com.example.tuitioninfoapp.models;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private String id;
    private String name;
    private String teacherId;
    private List<String> studentIds;
    private String materialUrl;


    @Override
    public String toString() {
        return name; // Display course name in spinner
    }

    // Constructors
    public Course() {}

    public Course(String id, String name) {
        this.id = id;
        this.name = name;
        this.studentIds = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getTeacherId() { return teacherId; }
    public List<String> getStudentIds() { return studentIds; }

    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    public void setStudentIds(List<String> studentIds) { this.studentIds = studentIds; }

    public void setId(String id) {
        this.id = id;
    }
    public String getMaterialUrl() { return materialUrl; }
    public void setMaterialUrl(String materialUrl) { this.materialUrl = materialUrl; }
}