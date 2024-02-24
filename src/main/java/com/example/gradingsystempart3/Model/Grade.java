package com.example.gradingsystempart3.Model;

public class Grade {
    private final int studentId;
    private final String studentFullName;
    private final Section section;
    private double grade;

    public Grade(int studentId, String studentFullName, Section section, double grade) {
        this.studentId = studentId;
        this.studentFullName = studentFullName;
        this.section = section;
        this.grade = grade;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public Section getSection() {
        return section;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
