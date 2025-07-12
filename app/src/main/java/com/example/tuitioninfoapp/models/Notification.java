package com.example.tuitioninfoapp.models;

import com.google.firebase.Timestamp;

public class Notification {
    private String id;
    private String studentId;
    private String message;
    private Timestamp timestamp;
    private boolean read;

    // Required empty constructor for Firestore deserialization
    public Notification() {}

    public Notification(String id, String studentId, String message, Timestamp timestamp, boolean read) {
        this.id = id;
        this.studentId = studentId;
        this.message = message;
        this.timestamp = timestamp;
        this.read = read;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return studentId;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return read;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String studentId) {
        this.studentId = studentId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}