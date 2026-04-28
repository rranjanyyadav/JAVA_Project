import java.sql.*;
import java.util.*;

/**
 * Student Management System (SMS) - Main Entry Point with all backend classes
 * 
 * This file contains:
 * - User, Student, Teacher (data models)
 * - DatabaseHelper (database operations)
 * - AuthManager (authentication)
 * - StudentManager, TeacherManager (business logic)
 * - Main class (entry point)
 */

// ==================== DATA MODELS ====================

/**
 * Represents a user in the Student Management System.
 */
class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}

/**
 * Represents a student in the Student Management System.
 */
class Student {
    private String name;
    private int id;
    private int semester;

    public Student(String name, int id, int semester) {
        this.name = name;
        this.id = id;
        this.semester = semester;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
}

/**
 * Represents a teacher in the Student Management System.
 */
class Teacher {
    private String name;
    private int id;
    private String subject;

    public Teacher(String name, int id, String subject) {
        this.name = name;
        this.id = id;
        this.subject = subject;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}

// ==================== DATABASE HELPER ====================

/**
 * Handles SQLite database operations for the Student Management System.
 */
class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:sms_database.db";
    private static Connection connection;

    public static void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found. Please add sqlite-jdbc to your classpath.");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    private static void createTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "semester INTEGER NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS teachers (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "subject TEXT NOT NULL)");
        } catch (SQLException e) {
            System.err.println("Table creation failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            initializeDatabase();
        }
        return connection;
    }

    public static void closeDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }

    public static ResultSet executeQuery(String query) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Query execution failed: " + e.getMessage());
            return null;
        }
    }

    public static boolean executeUpdate(String query) {
        try {
            Statement stmt = getConnection().createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Update execution failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean executePreparedUpdate(String query, Object... params) {
        try {
            java.sql.PreparedStatement pstmt = getConnection().prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, String.valueOf(params[i]));
            }
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Prepared update execution failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean isDatabaseAvailable() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}

// ==================== MANAGERS ====================

/**
 * Manages user authentication and user database.
 */
class AuthManager {
    private final List<User> users;

    public AuthManager() {
        this.users = new ArrayList<>();
        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            return;
        }

        try {
            String query = "SELECT * FROM users";
            ResultSet rs = DatabaseHelper.executeQuery(query);
            
            if (rs != null) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String role = rs.getString("role");
                    users.add(new User(username, password, role));
                }
                rs.close();
            }
            System.out.println("Loaded " + users.size() + " users from database");
        } catch (SQLException e) {
            System.err.println("Error loading users from database: " + e.getMessage());
        }
    }

    public void addUser(User user) {
        if (user != null) {
            users.add(user);
            saveUserToDatabase(user);
        }
    }

    private void saveUserToDatabase(User user) {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            System.err.println("Database not available for saving user: " + user.getUsername());
            return;
        }

        try {
            String query = "INSERT OR REPLACE INTO users (username, password, role) VALUES (?, ?, ?)";
            boolean success = DatabaseHelper.executePreparedUpdate(query, user.getUsername(), user.getPassword(), user.getRole());
            if (success) {
                System.out.println("Saved user to database: " + user.getUsername() + " (" + user.getRole() + ")");
            }
        } catch (Exception e) {
            System.err.println("Error saving user to database: " + e.getMessage());
        }
    }

    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}

/**
 * Manages the student database and related operations.
 */
class StudentManager {
    private final List<Student> students;

    public StudentManager() {
        this.students = new ArrayList<>();
        loadStudentsFromDatabase();
    }

    private void loadStudentsFromDatabase() {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            return;
        }

        try {
            String query = "SELECT * FROM students";
            ResultSet rs = DatabaseHelper.executeQuery(query);
            
            if (rs != null) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int semester = rs.getInt("semester");
                    students.add(new Student(name, id, semester));
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Error loading students from database: " + e.getMessage());
        }
    }

    public void addStudent(Student student) {
        if (student != null) {
            students.add(student);
            saveStudentToDatabase(student);
        }
    }

    private void saveStudentToDatabase(Student student) {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            return;
        }

        try {
            String query = "INSERT OR REPLACE INTO students (id, name, semester) VALUES (?, ?, ?)";
            DatabaseHelper.executePreparedUpdate(query, String.valueOf(student.getId()), student.getName(), String.valueOf(student.getSemester()));
        } catch (Exception e) {
            System.err.println("Error saving student to database: " + e.getMessage());
        }
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student findById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    public void updateById(int oldId, int newId) {
        Student student = findById(oldId);
        if (student != null) {
            student.setId(newId);
            updateStudentInDatabase(student);
        }
    }

    private void updateStudentInDatabase(Student student) {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            return;
        }

        try {
            String query = "UPDATE students SET id = ? WHERE name = ?";
            DatabaseHelper.executePreparedUpdate(query, String.valueOf(student.getId()), student.getName());
        } catch (Exception e) {
            System.err.println("Error updating student in database: " + e.getMessage());
        }
    }

    public void updateStudent(int oldId, String newName, int newId, int newSemester) {
        for (Student student : students) {
            if (student.getId() == oldId) {
                student.setName(newName);
                student.setId(newId);
                student.setSemester(newSemester);
                updateStudentInDatabase(student);
                return;
            }
        }
    }
}

/**
 * Manages the teacher database and related operations.
 */
class TeacherManager {
    private final List<Teacher> teachers;

    public TeacherManager() {
        this.teachers = new ArrayList<>();
        loadTeachersFromDatabase();
    }

    private void loadTeachersFromDatabase() {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            return;
        }

        try {
            String query = "SELECT * FROM teachers";
            ResultSet rs = DatabaseHelper.executeQuery(query);
            
            if (rs != null) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String subject = rs.getString("subject");
                    teachers.add(new Teacher(name, id, subject));
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Error loading teachers from database: " + e.getMessage());
        }
    }

    public void addTeacher(Teacher teacher) {
        if (teacher != null) {
            teachers.add(teacher);
            saveTeacherToDatabase(teacher);
        }
    }

    private void saveTeacherToDatabase(Teacher teacher) {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            return;
        }

        try {
            String query = "INSERT OR REPLACE INTO teachers (id, name, subject) VALUES (?, ?, ?)";
            DatabaseHelper.executePreparedUpdate(query, String.valueOf(teacher.getId()), teacher.getName(), teacher.getSubject());
        } catch (Exception e) {
            System.err.println("Error saving teacher to database: " + e.getMessage());
        }
    }

    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers);
    }

    public Teacher findById(int id) {
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id) {
                return teacher;
            }
        }
        return null;
    }
}

// ==================== MAIN CLASS ====================

/**
 * Student Management System (SMS) - Main Entry Point
 */
public class SMS_Consolidated {

    public static void main(String[] args) {
        // Initialize database
        DatabaseHelper.initializeDatabase();

        // Add shutdown hook to close database gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseHelper.closeDatabase();
        }));

        // Initialize managers
        StudentManager studentManager = new StudentManager();
        TeacherManager teacherManager = new TeacherManager();
        AuthManager authManager = new AuthManager();
        
        // Add default users if they don't exist
        if (authManager.login("student1", "123") == null) {
            authManager.addUser(new User("student1", "123", "student"));
        }
        if (authManager.login("teacher1", "123") == null) {
            authManager.addUser(new User("teacher1", "123", "teacher"));
        }
        if (authManager.login("admin1", "123") == null) {
            authManager.addUser(new User("admin1", "123", "admin"));
        }

        // Launch login UI with all managers
        new LoginUI(authManager, studentManager, teacherManager);
    }
}
