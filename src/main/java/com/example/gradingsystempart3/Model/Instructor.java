package com.example.gradingsystempart3.Model;

public class Instructor extends User {
    private int instructorId;
    public Instructor(String username, String password, String firstName, String lastName) {
        super(username, password,firstName,lastName, Role.INSTRUCTOR);
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }
}
