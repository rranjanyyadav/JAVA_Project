import java.util.*;
import java.sql.*;

/**
 * Manages user authentication and user database.
 */
public class AuthManager {
    private final List<User> users;

    /**
     * Creates a new AuthManager with an empty user list.
     */
    public AuthManager() {
        this.users = new ArrayList<>();
        loadUsersFromDatabase();
    }

    /**
     * Loads users from the database.
     */
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
            e.printStackTrace();
        }
    }

    /**
     * Adds a user to the authentication system.
     * @param user the user to add
     */
    public void addUser(User user) {
        if (user != null) {
            users.add(user);
            saveUserToDatabase(user);
        }
    }

    /**
     * Saves a single user to the database.
     */
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
            } else {
                System.err.println("Failed to save user to database: " + user.getUsername());
            }
        } catch (Exception e) {
            System.err.println("Error saving user to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Authenticates a user with the given credentials.
     * @param username the username
     * @param password the password
     * @return the User if credentials are valid, null otherwise
     */
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        
        for (User user : users) {
            if (user.getUsername().equals(username) &&
                user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}