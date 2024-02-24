package com.example.gradingsystempart3.Service;

import com.example.gradingsystempart3.DAO.CourseDAO;
import com.example.gradingsystempart3.DAO.SectionDAO;
import com.example.gradingsystempart3.Exceptions.SectionNotFoundException;
import com.example.gradingsystempart3.Model.Section;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
@Service
public class SectionService {
    private final SectionDAO sectionDAO;

    public SectionService() {
        sectionDAO = new SectionDAO();
    }

    public boolean addSection(int courseId) {
        try {
            CourseDAO.checkCourseExists(courseId);
            return sectionDAO.insertSection(courseId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSection(int sectionId){
        try{
            SectionDAO.checkSectionExists(sectionId);
            return sectionDAO.deleteSection(sectionId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateCourseId(int sectionId,int newCourseId){
        return sectionDAO.updateCourseId(newCourseId,sectionId);
    }

    public Section getById(int sectionId){
        try {
            return sectionDAO.getById(sectionId);
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    public static boolean sectionExists(int sectionId){
        return SectionDAO.sectionExists(sectionId);
    }

    public static void checkSectionExists(int sectionId) throws SectionNotFoundException {
        SectionDAO.checkSectionExists(sectionId);
    }
    public List<String> getColumnsNames(){
        return sectionDAO.getColumnsNames();
    }

}
