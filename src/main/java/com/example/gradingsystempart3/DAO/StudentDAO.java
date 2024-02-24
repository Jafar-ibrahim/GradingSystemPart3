package com.example.gradingsystempart3.DAO;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Exceptions.SectionNotFoundException;
import com.example.gradingsystempart3.Exceptions.UserNotFoundException;
import com.example.gradingsystempart3.Model.User;
import com.example.gradingsystempart3.Model.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StudentDAO {

    private static final Database database = Database.getInstance();
    private final UserDAO userDAO = new UserDAO();
    private static final String TABLE_NAME = "student";


    public boolean insertStudent(int userId) throws SQLException {
        return database.insertRecord("student",userId);
    }

    public boolean deleteStudent(int studentId) throws SQLException {
        return database.deleteRecord(TABLE_NAME,studentId);
    }
    public boolean updateUserId(int studentId, int newUserId ){
        return database.updateRecord(TABLE_NAME,"user_id",newUserId,studentId);
    }

    public static boolean studentExists(int studentId){
        return database.recordExists("student",studentId);
    }

    public static void checkStudentExists( int studentId) throws UserNotFoundException {
        if(!studentExists(studentId))
            throw new UserNotFoundException();
    }

    public int getStudentId(int userId) throws SQLException {
        String sql = "SELECT student_id FROM student WHERE user_id = ?";
        int studentId = -1; // Default value if student not found

        try (Connection connection = database.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    studentId = resultSet.getInt("student_id");
                }
            }

        }
        return studentId;
    }

    public String getStudentFullName( int studentId) throws SQLException {
        String sql = "SELECT CONCAT(u.first_name, ' ', u.last_name) AS full_name " +
                "FROM user u " +
                "JOIN student i ON u.user_id = i.user_id " +
                "WHERE i.student_id = ?";

        try (Connection connection = database.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("full_name");
                }
            }

        }
        return null;
    }
    public User getById(int studentId) throws SQLException {
        try(ResultSet resultSet = database.readRecord(TABLE_NAME,studentId)){
            if(resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                return userDAO.getById(userId);
            }
            throw new SectionNotFoundException();
        }
    }

    public List<String> getColumnsNames(){
        return database.getTableColumnsNames(TABLE_NAME);
    }

}
