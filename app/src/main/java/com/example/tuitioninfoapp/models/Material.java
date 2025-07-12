package com.example.tuitioninfoapp.models;

public class Material {
    private String courseName;
    private String materialUrl;

    public Material() {
        // Required empty constructor for Firebase
    }

    public Material(String courseName, String materialUrl) {
        this.courseName = courseName;
        this.materialUrl = materialUrl;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }
}
