package com.example.tuitioninfoapp.models;
public class User {
    private String uid;
    private String name;
    private String email;
    private String role;

    public User() {}  // Needed for Firestore

    public User(String uid, String name, String email, String role) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.role = role;


    }
    private String assignedTeacherId;
    public String getAssignedTeacherId() { return assignedTeacherId; }
    public void setAssignedTeacherId(String assignedTeacherId) { this.assignedTeacherId = assignedTeacherId; }

    public String getUid() { return uid; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
