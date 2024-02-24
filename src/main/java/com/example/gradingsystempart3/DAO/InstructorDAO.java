package com.example.gradingsystempart3.DAO;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InstructorDAO {

    private static final Database database = Database.getInstance();
    private static final String TABLE_NAME = "instructor";


    public boolean insertInstructor(int userId) throws SQLException {
        return database.insertRecord("instructor",userId);
    }

    public String getInstructorFullName( int instructorId) {
        String sql = "SELECT CONCAT(u.first_name, ' ', u.last_name) AS full_name " +
                "FROM user u " +
                "JOIN instructor i ON u.user_id = i.user_id " +
                "WHERE i.instructor_id = ?";

        try (Connection connection = database.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, instructorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("full_name");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteInstructor(int instructorId) throws SQLException {
        return database.deleteRecord(TABLE_NAME,instructorId);
    }
    public boolean updateUserId(int instructorId, int newUserId ){
        return database.updateRecord(TABLE_NAME,"user_id",newUserId,instructorId);
    }
    public static boolean instructorExists(int instructorId){
        return database.recordExists("instructor",instructorId);
    }
    public static void checkInstructorExists(int instructorId) throws UserNotFoundException {
        if(!instructorExists(instructorId))
            throw new UserNotFoundException();
    }
    public int getInstructorId(int userId) throws SQLException {
        String sql = "SELECT instructor_id FROM instructor WHERE user_id = ?";
        int instructorId = -1; // Default value if instructor not found

        try (Connection connection = database.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    instructorId = resultSet.getInt("instructor_id");
                }
            }

        }
        return instructorId;
    }

    public List<String> getColumnsNames(){
        return database.getTableColumnsNames(TABLE_NAME);
    }
}
