import java.util.*;
import java.sql.*;

/**
 * Manages the teacher database and related operations.
 */
public class TeacherManager {
    private final List<Teacher> teachers;

    /**
     * Creates a new TeacherManager with an empty teacher list.
     */
    public TeacherManager() {
        this.teachers = new ArrayList<>();
        loadTeachersFromDatabase();
    }

    /**
     * Loads teachers from the database.
     */
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

    /**
     * Adds a teacher to the database.
     * @param teacher the teacher to add
     */
    public void addTeacher(Teacher teacher) {
        if (teacher != null) {
            teachers.add(teacher);
            saveTeacherToDatabase(teacher);
        }
    }

    /**
     * Saves a single teacher to the database.
     */
    private void saveTeacherToDatabase(Teacher teacher) {
        if (!DatabaseHelper.isDatabaseAvailable()) {
            return;
        }

        try {
            String query = "INSERT OR REPLACE INTO teachers (id, name, subject) VALUES (?, ?, ?)";
            DatabaseHelper.executePreparedUpdate(query, String.valueOf(teacher.getId()), teacher.getName(), teacher.getSubject());
        } catch (Exception e) {
            System.err.println("Error saving teacher to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all teachers.
     * @return a list of all teachers
     */
    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers);
    }

    /**
     * Finds a teacher by their ID.
     * @param id the teacher's ID
     * @return the Teacher if found, null otherwise
     */
    public Teacher findById(int id) {
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id) {
                return teacher;
            }
        }
        return null;
    }
}
