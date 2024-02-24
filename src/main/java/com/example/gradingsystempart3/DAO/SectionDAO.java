package com.example.gradingsystempart3.DAO;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Exceptions.*;
import com.example.gradingsystempart3.Model.Course;
import com.example.gradingsystempart3.Model.Section;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SectionDAO {

    private static final Database database = Database.getInstance();
    private final CourseDAO courseDAO = new CourseDAO();
    private static final String TABLE_NAME = "section";



    public boolean insertSection(int courseId) throws SQLException {
        return database.insertRecord(TABLE_NAME,courseId);
    }


    public boolean deleteSection(int sectionId) throws SQLException {
        return database.deleteRecord(TABLE_NAME,sectionId);
    }
    public boolean updateCourseId(int sectionId, int newCourseId ){
        return database.updateRecord(TABLE_NAME,"course_id",newCourseId,sectionId);
    }
    public static boolean sectionExists(int sectionId){
        return database.recordExists(TABLE_NAME,sectionId);
    }

    public static void checkSectionExists(int sectionId) throws SectionNotFoundException {
        if(!sectionExists(sectionId))
            throw new SectionNotFoundException();
    }
    public Section getById(int sectionId) throws SQLException {
        try(ResultSet resultSet = database.readRecord(TABLE_NAME,sectionId)){
            if(resultSet.next()) {
                int courseID = resultSet.getInt("course_id");
                Course course = courseDAO.getById(courseID);
                return new Section(sectionId, course);
            }
            throw new SectionNotFoundException();
        }
    }

    public List<String> getColumnsNames(){
        return database.getTableColumnsNames(TABLE_NAME);
    }
}
