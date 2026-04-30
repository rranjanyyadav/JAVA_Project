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

/**
 * Represents a course taught by a teacher.
 */
class Course {
    private int id;
    private String name;
    private int teacherId;
    private int credits;
    private String schedule;

    public Course(int id, String name, int teacherId, int credits, String schedule) {
        this.id = id;
        this.name = name;
        this.teacherId = teacherId;
        this.credits = credits;
        this.schedule = schedule;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
}

/**
 * Represents student grades/marks.
 */
class Grade {
    private int studentId;
    private int courseId;
    private double marks;
    private String grade;
    private String semester;

    public Grade(int studentId, int courseId, double marks, String grade, String semester) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
        this.grade = grade;
        this.semester = semester;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}

/**
 * Represents attendance records.
 */
class Attendance {
    private int studentId;
    private String date;
    private String status;

    public Attendance(int studentId, String date, String status) {
        this.studentId = studentId;
        this.date = date;
        this.status = status;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
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

            stmt.execute("CREATE TABLE IF NOT EXISTS courses (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "teacher_id INTEGER NOT NULL," +
                    "credits INTEGER NOT NULL," +
                    "schedule TEXT NOT NULL," +
                    "FOREIGN KEY (teacher_id) REFERENCES teachers(id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS grades (" +
                    "student_id INTEGER NOT NULL," +
                    "course_id INTEGER NOT NULL," +
                    "marks REAL NOT NULL," +
                    "grade TEXT NOT NULL," +
                    "semester TEXT NOT NULL," +
                    "PRIMARY KEY (student_id, course_id)," +
                    "FOREIGN KEY (student_id) REFERENCES students(id)," +
                    "FOREIGN KEY (course_id) REFERENCES courses(id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS attendance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id INTEGER NOT NULL," +
                    "date TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");
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

class CourseManager {
    private final List<Course> courses;

    public CourseManager() {
        this.courses = new ArrayList<>();
        loadCoursesFromDatabase();
    }

    private void loadCoursesFromDatabase() {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            return;
        }
        try {
            String query = "SELECT * FROM courses";
            ResultSet rs = DatabaseHelper.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int teacherId = rs.getInt("teacher_id");
                    int credits = rs.getInt("credits");
                    String schedule = rs.getString("schedule");
                    courses.add(new Course(id, name, teacherId, credits, schedule));
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Error loading courses from database: " + e.getMessage());
        }
    }

    public void addCourse(Course course) {
        if (course != null) {
            courses.add(course);
            saveCourseToDB(course);
        }
    }

    private void saveCourseToDB(Course course) {
        if (!DatabaseHelper.isDatabaseAvailable()) return;
        try {
            String query = "INSERT OR REPLACE INTO courses (id, name, teacher_id, credits, schedule) VALUES (?, ?, ?, ?, ?)";
            DatabaseHelper.executePreparedUpdate(query, String.valueOf(course.getId()), course.getName(), 
                String.valueOf(course.getTeacherId()), String.valueOf(course.getCredits()), course.getSchedule());
        } catch (Exception e) {
            System.err.println("Error saving course to database: " + e.getMessage());
        }
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    public Course findById(int id) {
        for (Course course : courses) {
            if (course.getId() == id) return course;
        }
        return null;
    }

    public List<Course> getCoursesByTeacher(int teacherId) {
        List<Course> result = new ArrayList<>();
        for (Course course : courses) {
            if (course.getTeacherId() == teacherId) result.add(course);
        }
        return result;
    }
}

class GradeManager {
    private final List<Grade> grades;

    public GradeManager() {
        this.grades = new ArrayList<>();
        loadGradesFromDatabase();
    }

    private void loadGradesFromDatabase() {
        if (!DatabaseHelper.isDatabaseAvailable()) return;
        try {
            String query = "SELECT * FROM grades";
            ResultSet rs = DatabaseHelper.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    int courseId = rs.getInt("course_id");
                    double marks = rs.getDouble("marks");
                    String grade = rs.getString("grade");
                    String semester = rs.getString("semester");
                    grades.add(new Grade(studentId, courseId, marks, grade, semester));
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Error loading grades from database: " + e.getMessage());
        }
    }

    public void addGrade(Grade grade) {
        if (grade != null) {
            grades.add(grade);
            saveGradeToDB(grade);
        }
    }

    private void saveGradeToDB(Grade grade) {
        if (!DatabaseHelper.isDatabaseAvailable()) return;
        try {
            String query = "INSERT OR REPLACE INTO grades (student_id, course_id, marks, grade, semester) VALUES (?, ?, ?, ?, ?)";
            DatabaseHelper.executePreparedUpdate(query, String.valueOf(grade.getStudentId()), String.valueOf(grade.getCourseId()),
                String.valueOf(grade.getMarks()), grade.getGrade(), grade.getSemester());
        } catch (Exception e) {
            System.err.println("Error saving grade to database: " + e.getMessage());
        }
    }

    public List<Grade> getAllGrades() {
        return new ArrayList<>(grades);
    }

    public List<Grade> getStudentGrades(int studentId) {
        List<Grade> result = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getStudentId() == studentId) result.add(grade);
        }
        return result;
    }

    public double calculateGPA(int studentId) {
        List<Grade> studentGrades = getStudentGrades(studentId);
        if (studentGrades.isEmpty()) return 0.0;
        double total = 0.0;
        for (Grade grade : studentGrades) {
            total += convertGradeToPoints(grade.getGrade());
        }
        return total / studentGrades.size();
    }

    private double convertGradeToPoints(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
}

class AttendanceManager {
    private final List<Attendance> attendanceRecords;

    public AttendanceManager() {
        this.attendanceRecords = new ArrayList<>();
        loadAttendanceFromDatabase();
    }

    private void loadAttendanceFromDatabase() {
        if (!DatabaseHelper.isDatabaseAvailable()) return;
        try {
            String query = "SELECT * FROM attendance";
            ResultSet rs = DatabaseHelper.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    String date = rs.getString("date");
                    String status = rs.getString("status");
                    attendanceRecords.add(new Attendance(studentId, date, status));
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Error loading attendance from database: " + e.getMessage());
        }
    }

    public void addAttendance(Attendance attendance) {
        if (attendance != null) {
            attendanceRecords.add(attendance);
            saveAttendanceToDB(attendance);
        }
    }

    private void saveAttendanceToDB(Attendance attendance) {
        if (!DatabaseHelper.isDatabaseAvailable()) return;
        try {
            String query = "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)";
            DatabaseHelper.executePreparedUpdate(query, String.valueOf(attendance.getStudentId()),
                attendance.getDate(), attendance.getStatus());
        } catch (Exception e) {
            System.err.println("Error saving attendance to database: " + e.getMessage());
        }
    }

    public List<Attendance> getStudentAttendance(int studentId) {
        List<Attendance> result = new ArrayList<>();
        for (Attendance record : attendanceRecords) {
            if (record.getStudentId() == studentId) result.add(record);
        }
        return result;
    }

    public double calculateAttendancePercentage(int studentId) {
        List<Attendance> records = getStudentAttendance(studentId);
        if (records.isEmpty()) return 0.0;
        int presentCount = 0;
        for (Attendance record : records) {
            if ("Present".equals(record.getStatus())) presentCount++;
        }
        return (presentCount * 100.0) / records.size();
    }

    public List<Attendance> getAllAttendance() {
        return new ArrayList<>(attendanceRecords);
    }
}

class CSVExporter {
    public static void exportStudents(List<Student> students, String filename) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write("ID,Name,Semester\n");
            for (Student s : students) {
                writer.write(s.getId() + "," + s.getName() + "," + s.getSemester() + "\n");
            }
            System.out.println("Students exported to " + filename);
        } catch (Exception e) {
            System.err.println("Error exporting students: " + e.getMessage());
        }
    }

    public static void exportTeachers(List<Teacher> teachers, String filename) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write("ID,Name,Subject\n");
            for (Teacher t : teachers) {
                writer.write(t.getId() + "," + t.getName() + "," + t.getSubject() + "\n");
            }
            System.out.println("Teachers exported to " + filename);
        } catch (Exception e) {
            System.err.println("Error exporting teachers: " + e.getMessage());
        }
    }

    public static void exportGrades(List<Grade> grades, String filename) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write("StudentID,CourseID,Marks,Grade,Semester\n");
            for (Grade g : grades) {
                writer.write(g.getStudentId() + "," + g.getCourseId() + "," + g.getMarks() + ","
                    + g.getGrade() + "," + g.getSemester() + "\n");
            }
            System.out.println("Grades exported to " + filename);
        } catch (Exception e) {
            System.err.println("Error exporting grades: " + e.getMessage());
        }
    }

    public static void exportAttendance(List<Attendance> records, String filename) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write("StudentID,Date,Status\n");
            for (Attendance a : records) {
                writer.write(a.getStudentId() + "," + a.getDate() + "," + a.getStatus() + "\n");
            }
            System.out.println("Attendance exported to " + filename);
        } catch (Exception e) {
            System.err.println("Error exporting attendance: " + e.getMessage());
        }
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
        CourseManager courseManager = new CourseManager();
        GradeManager gradeManager = new GradeManager();
        AttendanceManager attendanceManager = new AttendanceManager();
        
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
        new LoginUI(authManager, studentManager, teacherManager, courseManager, gradeManager, attendanceManager);
    }
}
