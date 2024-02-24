package com.example.gradingsystempart3.Model;

public class Section {
    private int id;
    private Course course;

    public Section(int id, Course course) {
        this.id = id;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
