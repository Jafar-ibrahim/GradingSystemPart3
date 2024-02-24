package com.example.gradingsystempart3.Service;

import com.example.gradingsystempart3.DAO.*;
import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Exceptions.DuplicateRecordException;
import com.example.gradingsystempart3.Model.Grade;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Service
public class GradeService {

    private static final Database database = Database.getInstance();
    private final GradeDAO gradeDAO;
    private final StudentDAO studentDAO;

    public GradeService() {
        gradeDAO = new GradeDAO();
        studentDAO = new StudentDAO();
    }

    public boolean addGrade(int studentId, int sectionId, double grade){
        try{
            StudentDAO.checkStudentExists(studentId);
            SectionDAO.checkSectionExists(sectionId);
            return gradeDAO.insertGrade(studentId,sectionId,grade);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteGrade(int studentId, int sectionId){
        try{
            return gradeDAO.deleteGrade(studentId,sectionId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateGrade(int studentId, int sectionId, double newGrade){
        try{
            StudentDAO.checkStudentExists(studentId);
            SectionDAO.checkSectionExists(sectionId);
            return gradeDAO.updateGrade(studentId,sectionId,newGrade);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Pair<Grade,Double>> getGradeReport(int studentId){
        List<Pair<Grade, Double>> grades = null;
        try {
            grades = gradeDAO.getStudentGrades(studentId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return grades;
    }

    public double getStudentAverage(int studentId) {
        double overallAvg = 0;
        try {
            overallAvg = gradeDAO.getStudentAverage(studentId);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return overallAvg ;
    }
    public String getCombinedInformation(int studentId){
        System.out.println("Combined Information for Student " + studentId + ":\n");

        return getGradeReport(studentId)+"\n"+ getStudentAverage(studentId);

    }

    public List<Grade> getSectionGrades(int sectionId) {
        try {
            return gradeDAO.getGradesBySectionId(sectionId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }

    }
    public double getMinGradeForSection(int sectionId) {
        List<Grade> sectionGrades = getSectionGrades(sectionId);
        if (sectionGrades.isEmpty()) {
            return 0;
        }
        return sectionGrades.stream().mapToDouble(Grade::getGrade).min().orElse(0);
    }

    public double getMaxGradeForSection(int sectionId) {
        List<Grade> sectionGrades = getSectionGrades(sectionId);
        if (sectionGrades.isEmpty()) {
            return 0;
        }
        return sectionGrades.stream().mapToDouble(Grade::getGrade).max().orElse(0);
    }

    public double getAverageGradeForSection(int sectionId) {
        List<Grade> sectionGrades = getSectionGrades(sectionId);
        if (sectionGrades.isEmpty()) {
            return 0;
        }
        return sectionGrades.stream().mapToDouble(Grade::getGrade).average().orElse(0);
    }
    public double getMedianGradeForSection(int sectionId) {
        List<Grade> sectionGrades = getSectionGrades(sectionId);

        if (sectionGrades.isEmpty()) {
            return 0;
        }
        sectionGrades.sort(Comparator.comparingDouble(Grade::getGrade));

        int size = sectionGrades.size();
        if (size % 2 == 0) {
            double mid1 = sectionGrades.get(size / 2 - 1).getGrade();
            double mid2 = sectionGrades.get(size / 2).getGrade();
            return (mid1 + mid2) / 2;
        } else {
            return sectionGrades.get(size / 2).getGrade();
        }
    }
    public static boolean gradeExists(int studentId , int sectionId){
        return GradeDAO.gradeExists(studentId,sectionId);
    }
    public static void checkGradeExists(int studentId , int sectionId) throws DuplicateRecordException {
        GradeDAO.checkGradeExists(studentId,sectionId);
    }
    public List<String> getColumnsNames(){
        return gradeDAO.getColumnsNames();
    }



}
