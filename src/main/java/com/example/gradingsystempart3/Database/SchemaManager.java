package com.example.gradingsystempart3.Database;

import com.example.gradingsystempart3.Service.*;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
@Component
public class SchemaManager {
    private final Database database = Database.getInstance();

    public void initializeTables(){
        try{
            createColumnOrderTable();
            createRoleTable();
            createUserTable();
            createAdminTable();
            createInstructorTable();
            createStudentTable();
            createCourseTable();
            createSectionTable();
            createGradeTable();
            createStudent_SectionTable();
            createInstructor_SectionTable();
        }catch (SQLException sqlException){
            System.out.println("Tables initialization failed...");
            System.out.println(sqlException.getMessage());
        }
    }
    public void dropAllTablesIfExist(){
        String[] tablesNames = new String[]{"column_order","instructor_section","student_section","grade","section","course","student","instructor","admin","user","role"};
        try(Connection connection = database.getDatabaseConnection();
            Statement statement = connection.createStatement()){

            for(String table : tablesNames){
                statement.execute("DROP TABLE IF EXISTS "+table);
            }
        }catch (SQLException sqlException){
            System.out.println("Failed to drop tables");
            System.out.println(sqlException.getMessage());
        }
    }
    private void createColumnOrderTable() throws SQLException {
        try(Connection connection = database.getDatabaseConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE column_order (" +
                    "table_name VARCHAR(255) PRIMARY KEY," +
                    "column_names TEXT)");
        }
    }
    private void createRoleTable() throws SQLException {
        try(Connection connection = database.getDatabaseConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE role (" +
                    "role_id INT PRIMARY KEY," +
                    "name VARCHAR(255) UNIQUE NOT NULL)");
            statement.execute("INSERT INTO column_order (table_name, column_names)" +
                    "VALUES (\'role\', '[role_id,name]')");
        }
    }
    private void createUserTable() throws SQLException {
        try(Connection connection = database.getDatabaseConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE user (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(255) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "first_name VARCHAR(255)," +
                    "last_name VARCHAR(255)," +
                    "role_id INT," +
                    "FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE)");

            statement.execute("INSERT INTO column_order (table_name, column_names)" +
                    "VALUES (\'user\', '[user_id,username,password,first_name,last_name,role_id]')");
        }
    }
    private void createStudentTable() throws SQLException {
        try (Connection connection = database.getDatabaseConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS student (" +
                    "student_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT," +
                    "FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE)");

            statement.execute("INSERT INTO column_order (table_name, column_names)" +
                    "VALUES (\'student\', '[student_id,user_id]')");
        }
    }

    private void createInstructorTable() throws SQLException {
        try (Connection connection = database.getDatabaseConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS instructor (" +
                    "instructor_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT," +
                    "FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE)");

            statement.execute("INSERT INTO column_order (table_name, column_names)" +
                    "VALUES (\'instructor\', '[instructor_id,user_id]')");
        }
    }

    private void createAdminTable() throws SQLException {
        try (Connection connection = database.getDatabaseConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS admin (" +
                    "admin_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT," +
                    "FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE)");

            statement.execute("INSERT INTO column_order (table_name, column_names)" +
                    "VALUES (\'admin\', '[admin_id,user_id]')");
        }
    }
    private void createCourseTable() throws SQLException {
        try(Connection connection = database.getDatabaseConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE course (" +
                    "course_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "course_name VARCHAR(255) NOT NULL)");

            statement.execute("INSERT INTO column_order (table_name, column_names)" +
                    "VALUES (\'course\', '[course_id,course_name]')");
        }
    }
    private void createGradeTable() throws SQLException {
        try(Connection connection = database.getDatabaseConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE grade (" +
                    "student_id INT," +
                    "section_id INT," +
                    "grade DOUBLE," +
                    "PRIMARY KEY (student_id, section_id)," +
                    "FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE," +
                    "FOREIGN KEY (section_id) REFERENCES section(section_id) ON DELETE CASCADE)");

            statement.execute("INSERT INTO column_order (table_name, column_names)" +
                    "VALUES (\'grade\', '[student_id,section_id,grade]')");

        }
    }
    private void createSectionTable() throws SQLException {
        try(Connection connection = database.getDatabaseConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE section (" +
                    "section_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "course_id INT," +
                    /*"course_name VARCHAR(255)," +*/
                    "FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE)");

            statement.execute("INSERT INTO column_order (table_name, column_names)" +
                    "VALUES (\'section\', '[section_id,course_id]')");
        }
    }
    private void createStudent_SectionTable() throws SQLException {
        try(Connection connection = database.getDatabaseConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE student_section (" +
                    "student_id INT," +
                    "section_id INT," +
                    "PRIMARY KEY (student_id, section_id)," +
                    "FOREIGN KEY (student_id) REFERENCES user(user_id)," +
                    "FOREIGN KEY (section_id) REFERENCES section(section_id) ON DELETE CASCADE)");

            statement.execute("INSERT INTO column_order (table_name, column_names)" +
                    "VALUES (\'student_section\', '[student_id,section_id]')");
        }
    }
    private void createInstructor_SectionTable() throws SQLException {
        try(Connection connection = database.getDatabaseConnection();
            Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE instructor_section (" +
                    "instructor_id INT," +
                    "section_id INT," +
                    "PRIMARY KEY (instructor_id, section_id)," +
                    "FOREIGN KEY (instructor_id) REFERENCES user(user_id)," +
                    "FOREIGN KEY (section_id) REFERENCES section(section_id) ON DELETE CASCADE)");

            statement.execute("INSERT INTO column_order (table_name, column_names)" +
                    "VALUES (\'instructor_section\', '[instructor_id,section_id]')");
        }
    }
    public void addDummyData()  {
        System.out.println("Inserting dummy data ...");
        RoleService roleService = new RoleService();
        UserService userService = new UserService();
        CourseService courseService = new CourseService();
        SectionService sectionService = new SectionService();
        EnrollmentService enrollmentService = new EnrollmentService();
        GradeService gradeService = new GradeService();

        try (Connection connection = database.getDatabaseConnection()) {
            roleService.addRole(1, "admin");
            roleService.addRole(2, "instructor");
            roleService.addRole(3, "student");

            String[] names = new String[]{"Jafar","Omar","Ahmad","Fatima","Sara","sffs","sdfsdf","saif","yousef"};
            userService.addAdmin("Admin1","password" , "Jafar", "Ibrahim");
            userService.addInstructor("Instructor1","password" , "Ibrahim", "Mohammad");
            userService.addInstructor("Instructor2","password" , "Omar", "Salim");
            userService.addInstructor("Instructor3","password" , "FirstName", "LastName");

            for (int i = 1; i <= names.length; i++) {
                userService.addStudent(names[i-1], "password" + i, "FirstName" + i, "LastName" + i);
            }

            String[] courses = new String[]{"Data Structures","Parallel Processing","Java Programming","Operating Systems","Networks"};
            for (int i = 1; i <= 5; i++) {
                courseService.addCourse(courses[i-1]);
            }

            sectionService.addSection(1 );
            sectionService.addSection( 1 );
            sectionService.addSection(2 );
            sectionService.addSection(3 );
            sectionService.addSection(4 );
            sectionService.addSection(5 );

            enrollmentService.addStudentToSection(1, 1);
            enrollmentService.addStudentToSection(2, 1);
            enrollmentService.addStudentToSection(3, 2);
            enrollmentService.addStudentToSection(4, 2);
            enrollmentService.addStudentToSection(1, 3);
            enrollmentService.addStudentToSection(2, 3);
            enrollmentService.addStudentToSection(3, 3);
            enrollmentService.addStudentToSection(8, 4);
            enrollmentService.addStudentToSection(9, 4);

            enrollmentService.addInstructorToSection(1,1);
            enrollmentService.addInstructorToSection(2,2);
            enrollmentService.addInstructorToSection(2,3);
            enrollmentService.addInstructorToSection(3,4);

            Random random = new Random();
            gradeService.addGrade(1, 1,random.nextInt(101));
            gradeService.addGrade(2, 1,random.nextInt(101));
            gradeService.addGrade(3, 2,random.nextInt(101));
            gradeService.addGrade(4, 2,random.nextInt(101));
            gradeService.addGrade(1, 3,random.nextInt(101));
            gradeService.addGrade(2, 3,random.nextInt(101));
            gradeService.addGrade(3, 3,random.nextInt(101));
            gradeService.addGrade(8, 4,random.nextInt(101));
            gradeService.addGrade(9, 4,random.nextInt(101));



            System.out.println("Dummy data added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
