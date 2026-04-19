import java.sql.*;

/**
 * Handles SQLite database operations for the Student Management System.
 */
public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:sms_database.db";
    private static Connection connection;

    /**
     * Initializes the database and creates tables if they don't exist.
     */
    public static void initializeDatabase() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found. Please add sqlite-jdbc to your classpath.");
            System.err.println("Download from: https://github.com/xerial/sqlite-jdbc/releases");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    /**
     * Creates necessary database tables.
     */
    private static void createTables() {
        try (Statement stmt = connection.createStatement()) {
            // Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL)");

            // Students table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "semester INTEGER NOT NULL)");

            // Teachers table
            stmt.execute("CREATE TABLE IF NOT EXISTS teachers (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "subject TEXT NOT NULL)");
        } catch (SQLException e) {
            System.err.println("Table creation failed: " + e.getMessage());
        }
    }

    /**
     * Gets the database connection.
     */
    public static Connection getConnection() {
        if (connection == null) {
            initializeDatabase();
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public static void closeDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }

    /**
     * Executes a SELECT query and returns the result set.
     */
    public static ResultSet executeQuery(String query) {
        try {
            Statement stmt = getConnection().createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Query execution failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Executes an UPDATE, INSERT, or DELETE query.
     */
    public static boolean executeUpdate(String query) {
        try {
            Statement stmt = getConnection().createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Update execution failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executes an INSERT or UPDATE with parameterized query.
     */
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
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if database connection is available.
     */
    public static boolean isDatabaseAvailable() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
