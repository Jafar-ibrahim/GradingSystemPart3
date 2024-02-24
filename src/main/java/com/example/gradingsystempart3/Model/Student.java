package com.example.gradingsystempart3.Model;



public class Student extends User {
    private int studentId;
    public Student(String username, String password, String firstName, String lastName) {
        super(username, password,firstName,lastName, Role.STUDENT);
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
