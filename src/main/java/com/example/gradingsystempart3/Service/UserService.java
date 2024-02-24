package com.example.gradingsystempart3.Service;

import com.example.gradingsystempart3.DAO.*;
import com.example.gradingsystempart3.Model.User;
import com.example.gradingsystempart3.Model.UserDTO;
import com.example.gradingsystempart3.Util.PasswordAuthenticator;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final AdminDAO adminDAO;
    private final InstructorDAO instructorDAO;
    private final StudentDAO studentDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.adminDAO = new AdminDAO();
        this.instructorDAO = new InstructorDAO();
        this.studentDAO = new StudentDAO();
    }

    public String addAdmin(String username, String password, String firstName, String lastName){
        try {
            int userId = userDAO.insertUser(username, password, firstName, lastName,1);
            adminDAO.insertAdmin(userId);
            return "Admin added successfully.";
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return "Admin addition failed.";
        }

    }
    public boolean addStudent( String username, String password, String firstName, String lastName){
        try {
            int userId = userDAO.insertUser(username, password, firstName, lastName,3);
            return studentDAO.insertStudent(userId);
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean userExists(int userId){
        return UserDAO.userExists(userId);
    }
    public static boolean studentExists(int studentId){
        return StudentDAO.studentExists(studentId);
    }
    public static boolean instructorExists(int instructorId){
        return InstructorDAO.instructorExists(instructorId);
    }
    public static void checkUserExists(int userId) throws SQLException {
        UserDAO.checkUserExists(userId);
    }
    

    public boolean addInstructor( String username, String password, String firstName, String lastName) {
        try {
            int userId = userDAO.insertUser(username, password, firstName, lastName,2);
            return instructorDAO.insertInstructor(userId);
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteAdmin(int adminId) {
        try {
            return adminDAO.deleteAdmin(adminId);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteUser(int userId) {
        try {
            return userDAO.deleteUser(userId);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }

    }
    public boolean deleteInstructor(int instructorId) {
        try {
            return instructorDAO.deleteInstructor(instructorId);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }

    }
    public boolean deleteStudent(int studentId) {
        try {
            return studentDAO.deleteStudent( studentId);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateStudentUserId(int studentId, int newUserId ){
        return studentDAO.updateUserId(studentId,newUserId);
    }
    public boolean updateInstructorUserId(int instructorId, int newUserId ){
        return studentDAO.updateUserId(instructorId,newUserId);
    }

    public int authenticateUser(String username, String password) {
        int userId = getIdByUsername(username);
        if(userId == -1) return -1;
        String hashedPassword = getPassword(userId);
        if(PasswordAuthenticator.verifyPassword(password,hashedPassword))
            return userId;
        else
            return -1;
    }

    public int getUserRole(int userId){
        try {
            return userDAO.getRole(userId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    public String getPassword(int userId){
        try{
            return userDAO.getPassword(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NoSuchElementException();
        }
    }
    public String getUserFullName(int userId){
        try {
            return userDAO.getFullName(userId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public int getSpecificId(int userId , int roleId){
        try{
            if (roleId == 1) return adminDAO.getAdminId(userId);
            else if(roleId == 2) return instructorDAO.getInstructorId(userId);
            else if(roleId == 3) return studentDAO.getStudentId(userId);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    public User getById(int userId) {
        try {
            return userDAO.getById(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<User> getByUsername(String username) {
        try {
            int id = getIdByUsername(username);
            return Optional.ofNullable(userDAO.getById(id));
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public int getIdByUsername(String username){
        try {
            return userDAO.getIdByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<String> getUserColumnsNames(){
        return userDAO.getColumnsNames();
    }
    public List<String> getStudentColumnsNames(){
        return studentDAO.getColumnsNames();
    }
    public List<String> getInstructorColumnsNames(){
        return instructorDAO.getColumnsNames();
    }

}
