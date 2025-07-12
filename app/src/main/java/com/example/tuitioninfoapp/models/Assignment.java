package com.example.tuitioninfoapp.models;

import java.util.List;

public class Assignment {
    private String id;
    private String courseId;
    private String teacherId;
    private List<String> studentIds;

    public Assignment() {}

    public Assignment(String courseId, String teacherId, List<String> studentIds) {
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.studentIds = studentIds;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getCourseId() { return courseId; }
    public String getTeacherId() { return teacherId; }
    public List<String> getStudentIds() { return studentIds; }

    public void setId(String id) { this.id = id; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    public void setStudentIds(List<String> studentIds) { this.studentIds = studentIds; }
}