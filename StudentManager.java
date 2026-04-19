import java.util.*;
import java.sql.*;

/**
 * Manages the student database and related operations.
 */
public class StudentManager {
    private final List<Student> students;

    /**
     * Creates a new StudentManager with an empty student list.
     */
    public StudentManager() {
        this.students = new ArrayList<>();
        loadStudentsFromDatabase();
    }

    /**
     * Loads students from the database.
     */
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

    /**
     * Adds a student to the database.
     * @param student the student to add
     */
    public void addStudent(Student student) {
        if (student != null) {
            students.add(student);
            saveStudentToDatabase(student);
        }
    }

    /**
     * Saves a single student to the database.
     */
    private void saveStudentToDatabase(Student student) {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            return;
        }

        try {
            String query = "INSERT OR REPLACE INTO students (id, name, semester) VALUES (?, ?, ?)";
            DatabaseHelper.executePreparedUpdate(query, String.valueOf(student.getId()), student.getName(), String.valueOf(student.getSemester()));
        } catch (Exception e) {
            System.err.println("Error saving student to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all students.
     * @return a list of all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Finds a student by their ID.
     * @param id the student's ID
     * @return the Student if found, null otherwise
     */
    public Student findById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    /**
     * Updates a student's ID.
     * @param oldId the current student ID
     * @param newId the new student ID
     */
    public void updateById(int oldId, int newId) {
        Student student = findById(oldId);
        if (student != null) {
            student.setId(newId);
            updateStudentInDatabase(student);
        }
    }

    /**
     * Updates a student in the database.
     */
    private void updateStudentInDatabase(Student student) {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            return;
        }

        try {
            String query = "UPDATE students SET id = ? WHERE name = ?";
            DatabaseHelper.executePreparedUpdate(query, String.valueOf(student.getId()), student.getName());
        } catch (Exception e) {
            System.err.println("Error updating student in database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}