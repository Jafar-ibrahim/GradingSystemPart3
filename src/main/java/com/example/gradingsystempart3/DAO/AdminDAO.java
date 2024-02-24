package com.example.gradingsystempart3.DAO;

import com.example.gradingsystempart3.Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class AdminDAO {

    private static final Database database = Database.getInstance();
    private static final String TABLE_NAME = "admin";
    public boolean insertAdmin(int userId) throws SQLException {
        return database.insertRecord(TABLE_NAME,userId);
    }

    public boolean deleteAdmin(int adminId) throws SQLException {
        return database.deleteRecord(TABLE_NAME,adminId);
    }
    public boolean updateUserId(int userId, int newUserId ){
        return database.updateRecord(TABLE_NAME,"user_id",newUserId,userId);
    }

    public int getAdminId(int userId) throws SQLException {
        String sql = "SELECT admin_id FROM admin WHERE user_id = ?";
        int adminId = -1; // Default value if admin not found

        try (Connection connection = database.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    adminId = resultSet.getInt("admin_id");
                }
            }

        }

        return adminId;
    }

    public List<String> getColumnsNames(){
        return database.getTableColumnsNames(TABLE_NAME);
    }
}
