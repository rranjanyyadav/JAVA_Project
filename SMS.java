/**
 * Student Management System (SMS) - Main Entry Point
 * 
 * A system for managing student information with role-based access control.
 * Roles: Student, Teacher, Admin
 */
public class SMS {

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