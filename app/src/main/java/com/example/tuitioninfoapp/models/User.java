package com.example.tuitioninfoapp.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String role;
    private List<String> courses;

    @Override
    public String toString() {
        return name; // Display user name in spinner
    }

    // Constructors
    public User() {}

    public User(String id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.courses = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public List<String> getCourses() { return courses; }

    public void setCourses(List<String> courses) { this.courses = courses; }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return id;

    }
}