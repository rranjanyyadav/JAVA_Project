import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern Login UI for the Student Management System.
 */
public class LoginUI {
    private static final String ROLE_STUDENT = "student";
    private static final String ROLE_TEACHER = "teacher";
    private static final String ROLE_ADMIN = "admin";
    private static final int WINDOW_WIDTH = 450;
    private static final int WINDOW_HEIGHT = 500;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color BUTTON_COLOR = new Color(46, 204, 113);

    private StudentManager studentManager;
    private TeacherManager teacherManager;
    private AuthManager authManager;

    /**
     * Creates the login interface.
     * @param auth the authentication manager
     * @param studentManager the student manager
     * @param teacherManager the teacher manager
     */
    public LoginUI(AuthManager auth, StudentManager studentManager, TeacherManager teacherManager) {
        this.authManager = auth;
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
        createUI();
    }

    /**
     * Creates the UI.
     */
    private void createUI() {
        JFrame frame = new JFrame("Student Management System - Login");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Main panel with background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Username
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(userLabel, gbc);

        gbc.gridy = 1;
        JTextField userField = createStyledTextField();
        formPanel.add(userField, gbc);

        // Password
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(passLabel, gbc);

        gbc.gridy = 3;
        JPasswordField passField = createStyledPasswordField();
        formPanel.add(passField, gbc);

        // Login button
        gbc.gridy = 4;
        gbc.insets = new Insets(25, 0, 15, 0);
        JButton loginButton = createStyledButton("Login", BUTTON_COLOR);
        loginButton.addActionListener(e -> handleLogin(frame, userField, passField));
        formPanel.add(loginButton, gbc);

        // Demo credentials info
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 0, 0);
        JLabel infoLabel = new JLabel("<html>Demo: student1 / teacher1 / admin1 (Password: 123)</html>");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        infoLabel.setForeground(new Color(127, 140, 141));
        formPanel.add(infoLabel, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Handles the login process.
     */
    private void handleLogin(JFrame frame, JTextField userField, JPasswordField passField) {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User loggedIn = authManager.login(username, password);

        if (loggedIn != null) {
            openDashboard(loggedIn);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            passField.setText("");
        }
    }

    /**
     * Opens the appropriate dashboard based on user role.
     */
    private void openDashboard(User user) {
        String role = user.getRole();
        
        switch (role) {
            case ROLE_STUDENT:
                new StudentDashboard(studentManager, authManager, studentManager, teacherManager);
                break;
            case ROLE_TEACHER:
                new TeacherDashboard(studentManager, authManager, studentManager, teacherManager);
                break;
            case ROLE_ADMIN:
                new AdminDashboard(teacherManager, authManager, studentManager);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown role: " + role);
        }
    }

    /**
     * Creates a styled text field.
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        field.setPreferredSize(new Dimension(300, 35));
        return field;
    }

    /**
     * Creates a styled password field.
     */
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        field.setPreferredSize(new Dimension(300, 35));
        return field;
    }

    /**
     * Creates a styled button.
     */
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(10, 0, 10, 0));
        btn.setPreferredSize(new Dimension(300, 40));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
}