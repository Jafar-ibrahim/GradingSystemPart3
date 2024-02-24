package com.example.gradingsystempart3.Service;

import com.example.gradingsystempart3.DAO.*;
import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Model.Section;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Service
public class EnrollmentService {
    private static final Database database = Database.getInstance();
    private final InstructorSectionDAO instructorSectionDAO;
    private final StudentSectionDAO studentSectionDAO;
    private final InstructorDAO instructorDAO;
    private final CourseDAO courseDAO;

    public EnrollmentService() {
        instructorSectionDAO = new InstructorSectionDAO();
        instructorDAO = new InstructorDAO();
        studentSectionDAO = new StudentSectionDAO();
        courseDAO = new CourseDAO();
    }

    public boolean addStudentToSection(int studentId , int sectionId){
        try{
            StudentDAO.checkStudentExists(studentId);
            SectionDAO.checkSectionExists(sectionId);
            return studentSectionDAO.insertStudentSection(studentId,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean removeStudentFromSection(int studentId , int sectionId){
        try{
            StudentDAO.checkStudentExists(studentId);
            SectionDAO.checkSectionExists(sectionId);

            return studentSectionDAO.deleteStudentSection(studentId,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean addInstructorToSection(int InstructorId , int sectionId){
        try{
            InstructorDAO.checkInstructorExists(InstructorId);
            SectionDAO.checkSectionExists(sectionId);
            return instructorSectionDAO.insertInstructorSection(InstructorId,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean removeInstructorFromSection(int InstructorId , int sectionId){
        try{
            InstructorDAO.checkInstructorExists(InstructorId);
            SectionDAO.checkSectionExists(sectionId);
            return instructorSectionDAO.deleteInstructorSection(InstructorId,sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean instructorSectionExists(int instructorId, int sectionId){
        return InstructorSectionDAO.InstructorSectionExists(instructorId,sectionId);
    }
    public boolean studentSectionExists(int studentId, int sectionId){
        return StudentSectionDAO.StudentSectionExists(studentId,sectionId);
    }
    public List<Section> getStudentSections(int studentId){
        try {
           return studentSectionDAO.getStudentSections(studentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Section> getInstructorSections(int instructorId){
        try {
            return instructorSectionDAO.getInstructorSections(instructorId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Integer> getStudentIdsBySection(int sectionId){
        try {
            return studentSectionDAO.getStudentIdsBySection(sectionId);
        }catch (SQLException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



}
