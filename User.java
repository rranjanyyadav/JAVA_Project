/**
 * Represents a user in the Student Management System.
 */
public class User {
    private String username;
    private String password;
    private String role;

    /**
     * Creates a new User with the specified credentials and role.
     * @param username the username
     * @param password the password
     * @param role the user role (student, teacher, or admin)
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { 
        return username; 
    }

    public String getPassword() { 
        return password; 
    }

    public String getRole() { 
        return role; 
    }
}