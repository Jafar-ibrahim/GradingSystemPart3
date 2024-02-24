package com.example.gradingsystempart3.DAO;

import com.example.gradingsystempart3.Database.Database;
import com.example.gradingsystempart3.Exceptions.*;
import com.example.gradingsystempart3.Model.*;
import com.example.gradingsystempart3.Util.PasswordAuthenticator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class UserDAO {

    private static final Database database = Database.getInstance();
    private final RoleDAO roleDAO = new RoleDAO();
    private static final String TABLE_NAME = "user";


    public int insertUser(String username, String password,
                                  String firstName, String lastName, int roleId) throws SQLException {
        if(database.insertRecord(TABLE_NAME,username, PasswordAuthenticator.hashPassword(password),firstName,lastName,roleId))
            return getIdByUsername(username);

        return -1;
    }
    public int getIdByUsername(String username) throws SQLException{
        String query = "SELECT user_id FROM user WHERE username = '"+username+"'";
        try(ResultSet resultSet = database.executeQuery(query)){
            if(resultSet.next()){
                return resultSet.getInt("user_id");
            }
        }
        return -1;
    }
    public String getPassword(int userId) throws SQLException {
        try(ResultSet resultSet =  database.readRecord(TABLE_NAME,userId)) {
          if (resultSet.next()){
              return resultSet.getString("password");
          }
          throw new NoSuchElementException();
        }
    }
    public boolean deleteUser(int userId) throws SQLException {
        return database.deleteRecord(TABLE_NAME,userId);
    }

    public static boolean userExists(int userId){
        return database.recordExists(TABLE_NAME,userId);
    }
    public static void checkUserExists(int userId) throws SQLException {
        if(!userExists(userId))
            throw new UserNotFoundException();
    }

    public String getFullName(int userId) throws SQLException {
        String sql = "SELECT first_name, last_name FROM user WHERE user_id = ?";
        String fullName = null; // Default value if user not found

        try (Connection connection = database.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    fullName = firstName + " " + lastName;
                }
            }

        }

        return fullName;
    }
    public int getRole(int userId) throws SQLException {
        String sql = "SELECT role_id FROM user WHERE user_id = ?";
        int roleId = -1; // Default value if user not found or role not set

        try (Connection connection = database.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    roleId = resultSet.getInt("role_id");
                }
            }

        }
        return roleId;
    }
    /*public int authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT user_id FROM user WHERE username = ? AND password = ?";

        try (Connection connection = database.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // User authenticated, return user_id
                    return resultSet.getInt("user_id");
                } else {
                    // User not authenticated
                    return -1;
                }
            }

        }
    }*/

    public User getById(int userId) throws SQLException {
        try(ResultSet resultSet = database.readRecord(TABLE_NAME,userId)){
            if(resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Role role = roleDAO.getById(resultSet.getInt("role_id"));
                return new User(username,password,firstName,lastName,role);
            }
            throw new UserNotFoundException();
        }
    }

    public List<String> getColumnsNames(){
        return database.getTableColumnsNames(TABLE_NAME);
    }
}
