package com.example.gradingsystempart3.DAO;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Exceptions.*;
import com.example.gradingsystempart3.Model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDAO {

    private static final Database database = Database.getInstance();

    private static final String TABLE_NAME = "course";

    public boolean insertCourse(String courseName) {
        return database.insertRecord("course",courseName);
    }
    public boolean deleteCourse(int courseId) {
        return database.deleteRecord(TABLE_NAME,courseId);
    }
    public boolean updateCourseName(int courseId,String name ){
        return database.updateRecord(TABLE_NAME,"course_name",name,courseId);
    }

    public static boolean courseExists(int courseId){
        return database.recordExists("course",courseId);
    }
    public static void checkCourseExists(int courseId) throws CourseNotFoundException {
        if(!courseExists(courseId))
            throw new CourseNotFoundException();
    }

    public List<Map<String, Object>> getInstructorCourses(int instructorId) throws SQLException {
        String sql = "SELECT c.course_name, s.section_id " +
                "FROM instructor_section isec " +
                "JOIN section s ON isec.section_id = s.section_id " +
                "JOIN course c ON s.course_id = c.course_id " +
                "WHERE isec.instructor_id = ?";
        try (Connection connection = database.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, instructorId);

            List<Map<String, Object>> courses = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> courseMap = new HashMap<>();
                courseMap.put("course_name", resultSet.getString("course_name"));
                courseMap.put("section_id", resultSet.getInt("section_id"));
                courses.add(courseMap);
            }

            resultSet.close();
            return courses;
        }
    }
    public Course getById(int courseId) throws SQLException {
        try(ResultSet resultSet = database.readRecord(TABLE_NAME,courseId)){
            if(resultSet.next()) {
                String name = resultSet.getString("course_name");
                return new Course(courseId, name);
            }
            throw new CourseNotFoundException();
        }
    }
    public String getCourseName(int courseId) throws SQLException {
        String sql = "SELECT course_name FROM course WHERE course_id = ?";
        try (Connection connection = database.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next())
                    return resultSet.getString("course_name");
            }
        }
        return "N/A";
    }
    public List<String> getColumnsNames(){
        return database.getTableColumnsNames(TABLE_NAME);
    }

}
