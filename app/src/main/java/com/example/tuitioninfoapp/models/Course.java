package com.example.tuitioninfoapp.models;

import java.util.List;

// models/Course.java
public class Course {
    private String id;
    private String name;
    private String teacherId;
    private List<String> studentIds;

    public Course() {} // Firestore
    public Course(String name, String teacherId, List<String> studentIds) {
        this.name = name;
        this.teacherId = teacherId;
        this.studentIds = studentIds;
    }

    // getters & setters…
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

}

