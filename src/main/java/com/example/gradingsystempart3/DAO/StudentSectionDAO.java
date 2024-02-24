package com.example.gradingsystempart3.DAO;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Exceptions.RecordNotFoundException;
import com.example.gradingsystempart3.Model.Section;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentSectionDAO {

    private static final Database database = Database.getInstance();
    private final SectionDAO sectionDAO = new SectionDAO();
    private final GradeDAO gradeDAO = new GradeDAO();
    private static final String TABLE_NAME = "student_section";


    public boolean insertStudentSection(int studentId, int sectionId) throws SQLException {
        return database.insertRecord(TABLE_NAME,studentId,sectionId);
    }

    public boolean deleteStudentSection(int studentId, int sectionId) throws SQLException {
        return database.deleteRecord(TABLE_NAME,studentId,sectionId) &&
                gradeDAO.deleteStudentGradesFromSection(studentId ,sectionId);
    }

    public static boolean StudentSectionExists( int studentId, int sectionId){
        return database.recordExists(TABLE_NAME,studentId,sectionId);
    }
    public static void checkStudentSectionExists( int studentId, int sectionId) throws RecordNotFoundException{
        if(!StudentSectionExists(studentId,sectionId))
            throw new RecordNotFoundException();
    }

    public List<Section> getStudentSections(int studentId) throws SQLException {
        List<Section> sectionsList = new ArrayList<>();
        try(ResultSet resultSet = database.readRecord(TABLE_NAME,studentId)) {
            while (resultSet.next()){
                int section_id = resultSet.getInt("section_id");
                Section section = sectionDAO.getById(section_id);
                sectionsList.add(section);
            }
        }
        return sectionsList;
    }

    public List<Integer> getStudentIdsBySection(int sectionId) throws SQLException{
        String sql = "SELECT * FROM student_section WHERE section_id = ?";
        List<Integer> studentIds = new ArrayList<>();
        try(ResultSet resultSet = database.executeQuery(sql,sectionId)){
            while(resultSet.next()){
                int studentId = resultSet.getInt("student_id");
                studentIds.add(studentId);
            }
            return studentIds;
        }
    }
    public List<String> getColumnsNames(){
        return database.getTableColumnsNames(TABLE_NAME);
    }

}
