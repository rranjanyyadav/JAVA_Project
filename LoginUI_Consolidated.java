import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;

/**
 * Student Management System (SMS) - All UI Classes
 * 
 * This file contains all user interface components:
 * - LoginUI (login screen)
 * - AdminDashboard (admin panel)
 * - StudentDashboard (student view)
 * - TeacherDashboard (teacher panel)
 * - StudentEntryUI (student entry dialog)
 * - TeacherEntryUI (teacher entry dialog)
 * - RoundedBorder (utility class)
 */

// ==================== LOGIN UI ====================

/**
 * Modern Login UI for the Student Management System.
 */
class LoginUI {
    private static final String ROLE_STUDENT = "student";
    private static final String ROLE_TEACHER = "teacher";
    private static final String ROLE_ADMIN = "admin";
    private static final int WINDOW_WIDTH = 450;
    private static final int WINDOW_HEIGHT = 500;
    private static final Color PRIMARY_COLOR = new Color(44, 62, 80);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color BUTTON_COLOR = new Color(44, 62, 80);

    private StudentManager studentManager;
    private TeacherManager teacherManager;
    private AuthManager authManager;
    private CourseManager courseManager;
    private GradeManager gradeManager;
    private AttendanceManager attendanceManager;

    public LoginUI(AuthManager auth, StudentManager studentManager, TeacherManager teacherManager, 
                   CourseManager courseManager, GradeManager gradeManager, AttendanceManager attendanceManager) {
        this.authManager = auth;
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
        this.courseManager = courseManager;
        this.gradeManager = gradeManager;
        this.attendanceManager = attendanceManager;
        createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("Student Management System - Login");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(userLabel, gbc);

        gbc.gridy = 1;
        JTextField userField = createStyledTextField();
        formPanel.add(userField, gbc);

        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(passLabel, gbc);

        gbc.gridy = 3;
        JPasswordField passField = createStyledPasswordField();
        formPanel.add(passField, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(25, 0, 15, 0);
        JButton loginButton = createStyledButton("Login", BUTTON_COLOR);
        loginButton.addActionListener(e -> handleLogin(frame, userField, passField));
        formPanel.add(loginButton, gbc);

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

    private void openDashboard(User user) {
        String role = user.getRole();
        
        switch (role) {
            case ROLE_STUDENT:
                new StudentDashboard(studentManager, authManager, studentManager, teacherManager, courseManager, gradeManager, attendanceManager);
                break;
            case ROLE_TEACHER:
                new TeacherDashboard(studentManager, authManager, studentManager, teacherManager, courseManager, gradeManager, attendanceManager);
                break;
            case ROLE_ADMIN:
                new AdminDashboard(teacherManager, authManager, studentManager, courseManager, gradeManager, attendanceManager);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown role: " + role);
        }
    }

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

// ==================== ADMIN DASHBOARD ====================

/**
 * Enhanced Admin Dashboard with all administrative features.
 */
class AdminDashboard {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 750;
    private static final Color PRIMARY_COLOR = new Color(44, 62, 80);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color ACCENT_COLOR = new Color(149, 165, 176);
    private static final Color SUCCESS_COLOR = new Color(44, 62, 80);
    private static final Color WARNING_COLOR = new Color(127, 140, 141);
    private static final Color DANGER_COLOR = new Color(52, 73, 94);
    
    private TeacherManager teacherManager;
    private AuthManager authManager;
    private StudentManager studentManager;
    private CourseManager courseManager;
    private GradeManager gradeManager;
    private AttendanceManager attendanceManager;

    public AdminDashboard(TeacherManager teacherManager, AuthManager authManager, StudentManager studentManager,
                          CourseManager courseManager, GradeManager gradeManager, AttendanceManager attendanceManager) {
        this.teacherManager = teacherManager;
        this.authManager = authManager;
        this.studentManager = studentManager;
        this.courseManager = courseManager;
        this.gradeManager = gradeManager;
        this.attendanceManager = attendanceManager;
        
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        JLabel titleLabel = new JLabel("Administrator Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Scrollable Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Welcome Section
        gbc.gridy = 0;
        JLabel welcomeLabel = new JLabel("Welcome to Admin Panel");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        contentPanel.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        JLabel descLabel = new JLabel("Manage all system aspects and user data");
        descLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        descLabel.setForeground(new Color(127, 140, 141));
        contentPanel.add(descLabel, gbc);

        // Feature 1: Add Teachers
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel teachersLabel = new JLabel("👥 Teacher Management");
        teachersLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        teachersLabel.setForeground(ACCENT_COLOR);
        contentPanel.add(teachersLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(5, 10, 15, 10);
        JButton addTeachersBtn = createFeatureButton("Add New Teachers", PRIMARY_COLOR);
        addTeachersBtn.addActionListener(e -> new TeacherEntryUI(teacherManager, authManager, frame));
        contentPanel.add(addTeachersBtn, gbc);

        // Feature 2: View Teachers
        gbc.gridy = 4;
        JButton viewTeachersBtn = createFeatureButton("View All Teachers", ACCENT_COLOR);
        viewTeachersBtn.addActionListener(e -> showTeachersList());
        contentPanel.add(viewTeachersBtn, gbc);

        // Feature 3: View Students
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel studentsLabel = new JLabel("📚 Student Management");
        studentsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentsLabel.setForeground(ACCENT_COLOR);
        contentPanel.add(studentsLabel, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(5, 10, 15, 10);
        JButton viewStudentsBtn = createFeatureButton("View All Students", PRIMARY_COLOR);
        viewStudentsBtn.addActionListener(e -> new StudentDashboard(studentManager, authManager, studentManager, teacherManager, courseManager, gradeManager, attendanceManager));
        contentPanel.add(viewStudentsBtn, gbc);

        // Feature 4: System Statistics
        gbc.gridy = 7;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel reportsLabel = new JLabel("📊 System Reports");
        reportsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        reportsLabel.setForeground(ACCENT_COLOR);
        contentPanel.add(reportsLabel, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(5, 10, 15, 10);
        JButton statsBtn = createFeatureButton("View System Statistics", ACCENT_COLOR);
        statsBtn.addActionListener(e -> showSystemStatistics());
        contentPanel.add(statsBtn, gbc);

        // Feature 5: Generate Report
        gbc.gridy = 9;
        JButton reportBtn = createFeatureButton("Generate Report", PRIMARY_COLOR);
        reportBtn.addActionListener(e -> generateReport());
        contentPanel.add(reportBtn, gbc);

        // Feature 6: Settings
        gbc.gridy = 10;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel settingsLabel = new JLabel("⚙️ System Settings");
        settingsLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        settingsLabel.setForeground(ACCENT_COLOR);
        contentPanel.add(settingsLabel, gbc);

        gbc.gridy = 11;
        gbc.insets = new Insets(5, 10, 15, 10);
        JButton settingsBtn = createFeatureButton("Configure Settings", ACCENT_COLOR);
        settingsBtn.addActionListener(e -> showSettings());
        contentPanel.add(settingsBtn, gbc);

        // Logout Button
        gbc.gridy = 12;
        gbc.insets = new Insets(30, 10, 10, 10);
        JButton logoutBtn = createFeatureButton("Logout", DANGER_COLOR);
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI(authManager, studentManager, teacherManager, courseManager, gradeManager, attendanceManager);
        });
        contentPanel.add(logoutBtn, gbc);

        // Spacer
        gbc.gridy = 13;
        gbc.weighty = 1.0;
        contentPanel.add(Box.createVerticalGlue(), gbc);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void showTeachersList() {
        StringBuilder sb = new StringBuilder();
        sb.append("TEACHERS IN SYSTEM\n");
        sb.append("==================\n\n");

        java.util.List<Teacher> teachers = teacherManager.getAllTeachers();
        if (teachers.isEmpty()) {
            sb.append("No teachers registered yet.");
        } else {
            for (Teacher t : teachers) {
                sb.append(String.format("• %s\n", t.getName()));
                sb.append(String.format("  ID: %d | Subject: %s\n\n", t.getId(), t.getSubject()));
            }
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "Teachers List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSystemStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("SYSTEM STATISTICS\n");
        sb.append("=================\n\n");

        int studentCount = studentManager.getAllStudents().size();
        int teacherCount = teacherManager.getAllTeachers().size();
        int userCount = authManager.getAllUsers().size();

        sb.append(String.format("Total Students: %d\n", studentCount));
        sb.append(String.format("Total Teachers: %d\n", teacherCount));
        sb.append(String.format("Total Users: %d\n\n", userCount));

        sb.append("Student Distribution by Semester:\n");
        java.util.Map<Integer, Integer> semesterCount = new java.util.HashMap<>();
        for (Student s : studentManager.getAllStudents()) {
            semesterCount.put(s.getSemester(), semesterCount.getOrDefault(s.getSemester(), 0) + 1);
        }
        if (semesterCount.isEmpty()) {
            sb.append("No students registered\n");
        } else {
            for (int sem = 1; sem <= 8; sem++) {
                int count = semesterCount.getOrDefault(sem, 0);
                sb.append(String.format("  Semester %d: %d students\n", sem, count));
            }
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 350));

        JOptionPane.showMessageDialog(null, scrollPane, "System Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("SYSTEM REPORT\n");
        sb.append("=============\n");
        sb.append("Generated: ").append(new java.util.Date()).append("\n\n");

        sb.append("STUDENT RECORDS:\n");
        sb.append("----------------\n");
        java.util.List<Student> students = studentManager.getAllStudents();
        if (students.isEmpty()) {
            sb.append("No students in system\n");
        } else {
            sb.append(String.format("%-30s %-10s %s\n", "Name", "ID", "Semester"));
            sb.append("----------------------------------------------\n");
            for (Student s : students) {
                sb.append(String.format("%-30s %-10d %d\n", s.getName(), s.getId(), s.getSemester()));
            }
        }

        sb.append("\n\nTEACHER RECORDS:\n");
        sb.append("----------------\n");
        java.util.List<Teacher> teachers = teacherManager.getAllTeachers();
        if (teachers.isEmpty()) {
            sb.append("No teachers in system\n");
        } else {
            sb.append(String.format("%-30s %-10s %s\n", "Name", "ID", "Subject"));
            sb.append("----------------------------------------------\n");
            for (Teacher t : teachers) {
                sb.append(String.format("%-30s %-10d %s\n", t.getName(), t.getId(), t.getSubject()));
            }
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 10));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(null, scrollPane, "System Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSettings() {
        JDialog settingsDialog = new JDialog();
        settingsDialog.setTitle("System Settings");
        settingsDialog.setSize(400, 300);
        settingsDialog.setLocationRelativeTo(null);
        settingsDialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("System Configuration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(15));

        JLabel db = new JLabel("Database: SQLite");
        db.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(db);

        JLabel dbFile = new JLabel("File: sms_database.db");
        dbFile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(dbFile);

        panel.add(Box.createVerticalStrut(10));

        JLabel appVersion = new JLabel("Application: Student Management System v1.0");
        appVersion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(appVersion);

        JLabel javaVersion = new JLabel("Runtime: Java " + System.getProperty("java.version"));
        javaVersion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(javaVersion);

        panel.add(Box.createVerticalStrut(20));

        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(PRIMARY_COLOR);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setPreferredSize(new Dimension(100, 35));
        closeBtn.addActionListener(e -> settingsDialog.dispose());
        panel.add(closeBtn);

        settingsDialog.add(panel);
        settingsDialog.setVisible(true);
    }

    private JButton createFeatureButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setPreferredSize(new Dimension(250, 40));
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

// ==================== STUDENT DASHBOARD ====================

/**
 * Modern Student Dashboard UI for viewing student information.
 */
class StudentDashboard {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final String[] COLUMN_NAMES = {"Name", "ID", "Semester"};
    private static final Color PRIMARY_COLOR = new Color(44, 62, 80);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color BUTTON_COLOR = new Color(44, 62, 80);
    private static final Color ACCENT_COLOR = new Color(149, 165, 176);
    
    private AuthManager authManager;
    private StudentManager studentManager;
    private TeacherManager teacherManager;
    private CourseManager courseManager;
    private GradeManager gradeManager;
    private AttendanceManager attendanceManager;

    public StudentDashboard(StudentManager manager, AuthManager authManager, 
                           StudentManager studentManager, TeacherManager teacherManager,
                           CourseManager courseManager, GradeManager gradeManager, AttendanceManager attendanceManager) {
        this.authManager = authManager;
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
        this.courseManager = courseManager;
        this.gradeManager = gradeManager;
        this.attendanceManager = attendanceManager;
        
        JFrame frame = new JFrame("Student Dashboard");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        JLabel titleLabel = new JLabel("Student Records");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(BUTTON_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(189, 195, 199));
        table.setShowGrid(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton loadBtn = createStyledButton("Load Students", PRIMARY_COLOR);
        loadBtn.addActionListener(e -> loadStudentsToTable(model, manager));

        JButton logoutBtn = createStyledButton("Logout", ACCENT_COLOR);
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI(authManager, studentManager, teacherManager, courseManager, gradeManager, attendanceManager);
        });

        buttonPanel.add(loadBtn);
        buttonPanel.add(logoutBtn);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);

        loadStudentsToTable(model, manager);
    }

    private void loadStudentsToTable(DefaultTableModel model, StudentManager manager) {
        model.setRowCount(0);

        for (Student student : manager.getAllStudents()) {
            Object[] row = {
                student.getName(),
                student.getId(),
                student.getSemester()
            };
            model.addRow(row);
        }
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        btn.setPreferredSize(new Dimension(140, 35));
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

// ==================== TEACHER DASHBOARD ====================

/**
 * Modern Teacher Dashboard UI for adding students and updating student information with search.
 */
class TeacherDashboard {
    private static final int WINDOW_WIDTH = 550;
    private static final int WINDOW_HEIGHT = 700;
    private static final Color PRIMARY_COLOR = new Color(44, 62, 80);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color BUTTON_COLOR = new Color(44, 62, 80);
    private static final Color SUCCESS_COLOR = new Color(44, 62, 80);
    private static final Color ACCENT_COLOR = new Color(149, 165, 176);
    
    private AuthManager authManager;
    private StudentManager studentManager;
    private TeacherManager teacherManager;
    private CourseManager courseManager;
    private GradeManager gradeManager;
    private AttendanceManager attendanceManager;
    private Student currentStudent;

    public TeacherDashboard(StudentManager manager, AuthManager authManager,
                           StudentManager studentManager, TeacherManager teacherManager,
                           CourseManager courseManager, GradeManager gradeManager, AttendanceManager attendanceManager) {
        this.authManager = authManager;
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
        this.courseManager = courseManager;
        this.gradeManager = gradeManager;
        this.attendanceManager = attendanceManager;
        this.currentStudent = null;
        
        JFrame frame = new JFrame("Teacher Dashboard");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        JLabel titleLabel = new JLabel("Teacher Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel scrollablePanel = new JPanel();
        scrollablePanel.setBackground(Color.WHITE);
        scrollablePanel.setLayout(new GridBagLayout());
        scrollablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ===== SEARCH SECTION =====
        gbc.gridy = 0;
        JLabel searchTitle = new JLabel("Search & Update Student");
        searchTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchTitle.setForeground(PRIMARY_COLOR);
        scrollablePanel.add(searchTitle, gbc);

        gbc.gridy = 1;
        JLabel searchIdLabel = new JLabel("Enter Student ID to Search:");
        searchIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        scrollablePanel.add(searchIdLabel, gbc);

        gbc.gridy = 2;
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        
        JTextField searchIdField = createStyledTextField();
        searchPanel.add(searchIdField);
        searchPanel.add(Box.createHorizontalStrut(5));
        
        JButton searchBtn = createSmallButton("Search", PRIMARY_COLOR);
        searchPanel.add(searchBtn);
        scrollablePanel.add(searchPanel, gbc);

        // Display Section
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel detailsTitle = new JLabel("Student Details");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        detailsTitle.setForeground(PRIMARY_COLOR);
        scrollablePanel.add(detailsTitle, gbc);

        // Name field
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 10, 0);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        scrollablePanel.add(nameLabel, gbc);

        gbc.gridy = 5;
        JTextField nameField = createStyledTextField();
        nameField.setEnabled(false);
        scrollablePanel.add(nameField, gbc);

        // Current ID field
        gbc.gridy = 6;
        JLabel currentIdLabel = new JLabel("Current Student ID:");
        currentIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        scrollablePanel.add(currentIdLabel, gbc);

        gbc.gridy = 7;
        JTextField currentIdField = createStyledTextField();
        currentIdField.setEnabled(false);
        scrollablePanel.add(currentIdField, gbc);

        // Semester field
        gbc.gridy = 8;
        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        scrollablePanel.add(semesterLabel, gbc);

        gbc.gridy = 9;
        JTextField semesterField = createStyledTextField();
        scrollablePanel.add(semesterField, gbc);

        // ===== EDIT SECTION =====
        gbc.gridy = 10;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel editTitle = new JLabel("Edit Student Information");
        editTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editTitle.setForeground(PRIMARY_COLOR);
        scrollablePanel.add(editTitle, gbc);

        // New Name
        gbc.gridy = 11;
        gbc.insets = new Insets(10, 0, 10, 0);
        JLabel newNameLabel = new JLabel("New Name:");
        newNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        scrollablePanel.add(newNameLabel, gbc);

        gbc.gridy = 12;
        JTextField newNameField = createStyledTextField();
        scrollablePanel.add(newNameField, gbc);

        // New ID
        gbc.gridy = 13;
        JLabel newIdLabel = new JLabel("New Student ID:");
        newIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        scrollablePanel.add(newIdLabel, gbc);

        gbc.gridy = 14;
        JTextField newIdField = createStyledTextField();
        scrollablePanel.add(newIdField, gbc);

        // New Semester
        gbc.gridy = 15;
        JLabel newSemesterLabel = new JLabel("New Semester:");
        newSemesterLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        scrollablePanel.add(newSemesterLabel, gbc);

        gbc.gridy = 16;
        JTextField newSemesterField = createStyledTextField();
        scrollablePanel.add(newSemesterField, gbc);

        // ===== BUTTONS =====
        gbc.gridy = 17;
        gbc.insets = new Insets(25, 0, 10, 0);
        JPanel actionButtonPanel = new JPanel();
        actionButtonPanel.setBackground(Color.WHITE);
        actionButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton updateBtn = createStyledButton("Update", BUTTON_COLOR);
        updateBtn.addActionListener(e -> handleUpdate(frame, searchIdField, newNameField, newIdField, newSemesterField, manager));

        JButton clearBtn = createStyledButton("Clear", ACCENT_COLOR);
        clearBtn.addActionListener(e -> clearFields(searchIdField, nameField, currentIdField, semesterField, newNameField, newIdField, newSemesterField));

        actionButtonPanel.add(updateBtn);
        actionButtonPanel.add(clearBtn);
        scrollablePanel.add(actionButtonPanel, gbc);

        // Add Students Button
        gbc.gridy = 18;
        gbc.insets = new Insets(10, 0, 10, 0);
        JButton addStudentsBtn = createStyledButton("Add Students", PRIMARY_COLOR);
        addStudentsBtn.addActionListener(e -> new StudentEntryUI(manager, frame));
        scrollablePanel.add(addStudentsBtn, gbc);

        // Logout Button
        gbc.gridy = 19;
        JButton logoutBtn = createStyledButton("Logout", ACCENT_COLOR);
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI(authManager, studentManager, teacherManager, courseManager, gradeManager, attendanceManager);
        });
        scrollablePanel.add(logoutBtn, gbc);

        // Setup search button action
        searchBtn.addActionListener(e -> {
            try {
                String searchIdText = searchIdField.getText().trim();
                if (searchIdText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a Student ID to search", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int searchId = Integer.parseInt(searchIdText);
                Student foundStudent = manager.findById(searchId);

                if (foundStudent != null) {
                    currentStudent = foundStudent;
                    nameField.setText(foundStudent.getName());
                    currentIdField.setText(String.valueOf(foundStudent.getId()));
                    semesterField.setText(String.valueOf(foundStudent.getSemester()));
                    
                    newNameField.setText(foundStudent.getName());
                    newIdField.setText(String.valueOf(foundStudent.getId()));
                    newSemesterField.setText(String.valueOf(foundStudent.getSemester()));
                } else {
                    JOptionPane.showMessageDialog(frame, "Student not found with ID: " + searchId, "Not Found", JOptionPane.WARNING_MESSAGE);
                    clearFields(searchIdField, nameField, currentIdField, semesterField, newNameField, newIdField, newSemesterField);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid numeric ID", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void handleUpdate(JFrame frame, JTextField searchIdField, JTextField newNameField, 
                             JTextField newIdField, JTextField newSemesterField, StudentManager manager) {
        try {
            if (currentStudent == null) {
                JOptionPane.showMessageDialog(frame, "Please search for a student first", "No Student Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String newName = newNameField.getText().trim();
            String newIdText = newIdField.getText().trim();
            String newSemesterText = newSemesterField.getText().trim();

            if (newName.isEmpty() || newIdText.isEmpty() || newSemesterText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int newId = Integer.parseInt(newIdText);
            int newSemester = Integer.parseInt(newSemesterText);

            if (newSemester < 1 || newSemester > 8) {
                JOptionPane.showMessageDialog(frame, "Semester must be between 1 and 8", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            manager.updateStudent(currentStudent.getId(), newName, newId, newSemester);
            JOptionPane.showMessageDialog(frame, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            currentStudent = null;
            searchIdField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for ID and Semester", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
        currentStudent = null;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        field.setPreferredSize(new Dimension(300, 32));
        return field;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setPreferredSize(new Dimension(250, 38));
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

    private JButton createSmallButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(5, 15, 5, 15));
        btn.setPreferredSize(new Dimension(80, 32));
        btn.setMaximumSize(new Dimension(80, 32));
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

// ==================== STUDENT ENTRY UI ====================

/**
 * UI for entering student details. Called by teachers after login.
 */
class StudentEntryUI {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color ACCENT_COLOR = new Color(44, 62, 80);
    private static final Color PRIMARY_COLOR = new Color(44, 62, 80);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private StudentManager studentManager;
    private JTextArea studentList;

    public StudentEntryUI(StudentManager studentManager, JFrame parent) {
        this.studentManager = studentManager;
        createUI(parent);
    }

    private void createUI(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Add Students", true);
        dialog.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ACCENT_COLOR);
        headerPanel.setBorder(new RoundedBorder(10, ACCENT_COLOR));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titleLabel = new JLabel("Add Student Details");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new RoundedBorder(8, new Color(200, 200, 200)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(LABEL_FONT);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField nameField = createStyledTextField();
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(LABEL_FONT);
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField idField = createStyledTextField();
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel semesterLabel = new JLabel("Semester:");
        semesterLabel.setFont(LABEL_FONT);
        formPanel.add(semesterLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField semesterField = createStyledTextField();
        formPanel.add(semesterField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 10, 15);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton addBtn = createStyledButton("Add Student", PRIMARY_COLOR);
        addBtn.addActionListener(e -> {
            addStudent(dialog, nameField, idField, semesterField);
            nameField.setText("");
            idField.setText("");
            semesterField.setText("");
            nameField.requestFocus();
        });

        JButton backBtn = createStyledButton("Back", ACCENT_COLOR);
        backBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(addBtn);
        buttonPanel.add(backBtn);
        formPanel.add(buttonPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel listPanel = new JPanel();
        listPanel.setBackground(Color.WHITE);
        listPanel.setLayout(new BorderLayout());

        JLabel listLabel = new JLabel("Added Students:");
        listLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        listPanel.add(listLabel, BorderLayout.NORTH);

        studentList = new JTextArea();
        studentList.setEditable(false);
        studentList.setFont(new Font("Courier New", Font.PLAIN, 11));
        studentList.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(studentList);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        formPanel.add(listPanel, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        dialog.add(mainPanel);
        dialog.setVisible(true);

        updateStudentListDisplay();
    }

    private void addStudent(JDialog dialog, JTextField nameField, JTextField idField, JTextField semesterField) {
        try {
            String name = nameField.getText().trim();
            String idText = idField.getText().trim();
            String semesterText = semesterField.getText().trim();

            if (name.isEmpty() || idText.isEmpty() || semesterText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idText);
            int semester = Integer.parseInt(semesterText);

            if (semester < 1 || semester > 8) {
                JOptionPane.showMessageDialog(dialog, "Semester must be between 1 and 8", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Student student = new Student(name, id, semester);
            studentManager.addStudent(student);
            updateStudentListDisplay();
            JOptionPane.showMessageDialog(dialog, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "ID and Semester must be numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudentListDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Student student : studentManager.getAllStudents()) {
            sb.append(String.format("• %s (ID: %d, Sem: %d)\n", 
                student.getName(), student.getId(), student.getSemester()));
        }
        studentList.setText(sb.toString());
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(LABEL_FONT);
        field.setBorder(new LineBorder(new Color(180, 180, 180), 1));
        return field;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new RoundedBorder(6, color));
        btn.setPreferredSize(new Dimension(120, 35));
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

// ==================== TEACHER ENTRY UI ====================

/**
 * UI for entering teacher details. Called by admins after login.
 */
class TeacherEntryUI {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color ACCENT_COLOR = new Color(44, 62, 80);
    private static final Color PRIMARY_COLOR = new Color(44, 62, 80);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private TeacherManager teacherManager;
    private AuthManager authManager;
    private JTextArea teacherList;

    public TeacherEntryUI(TeacherManager teacherManager, AuthManager authManager, JFrame parent) {
        this.teacherManager = teacherManager;
        this.authManager = authManager;
        createUI(parent);
    }

    private void createUI(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Add Teachers", true);
        dialog.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ACCENT_COLOR);
        headerPanel.setBorder(new RoundedBorder(10, ACCENT_COLOR));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titleLabel = new JLabel("Add Teacher Details");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new RoundedBorder(8, new Color(200, 200, 200)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(LABEL_FONT);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField nameField = createStyledTextField();
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel idLabel = new JLabel("Teacher ID:");
        idLabel.setFont(LABEL_FONT);
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField idField = createStyledTextField();
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(LABEL_FONT);
        formPanel.add(subjectLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField subjectField = createStyledTextField();
        formPanel.add(subjectField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(LABEL_FONT);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField usernameField = createStyledTextField();
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(LABEL_FONT);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(LABEL_FONT);
        passwordField.setBorder(new LineBorder(new Color(180, 180, 180), 1));
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 10, 15);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton addBtn = createStyledButton("Add Teacher", PRIMARY_COLOR);
        addBtn.addActionListener(e -> {
            addTeacher(dialog, nameField, idField, subjectField, usernameField, passwordField);
            nameField.setText("");
            idField.setText("");
            subjectField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            nameField.requestFocus();
        });

        JButton backBtn = createStyledButton("Back", ACCENT_COLOR);
        backBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(addBtn);
        buttonPanel.add(backBtn);
        formPanel.add(buttonPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel listPanel = new JPanel();
        listPanel.setBackground(Color.WHITE);
        listPanel.setLayout(new BorderLayout());

        JLabel listLabel = new JLabel("Added Teachers:");
        listLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        listPanel.add(listLabel, BorderLayout.NORTH);

        teacherList = new JTextArea();
        teacherList.setEditable(false);
        teacherList.setFont(new Font("Courier New", Font.PLAIN, 11));
        teacherList.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(teacherList);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        formPanel.add(listPanel, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        dialog.add(mainPanel);
        dialog.setVisible(true);

        updateTeacherListDisplay();
    }

    private void addTeacher(JDialog dialog, JTextField nameField, JTextField idField, JTextField subjectField,
                           JTextField usernameField, JPasswordField passwordField) {
        try {
            String name = nameField.getText().trim();
            String idText = idField.getText().trim();
            String subject = subjectField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || idText.isEmpty() || subject.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idText);

            Teacher teacher = new Teacher(name, id, subject);
            teacherManager.addTeacher(teacher);

            User teacherUser = new User(username, password, "teacher");
            authManager.addUser(teacherUser);

            updateTeacherListDisplay();
            JOptionPane.showMessageDialog(dialog, "Teacher added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Teacher ID must be a number", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTeacherListDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Teacher teacher : teacherManager.getAllTeachers()) {
            sb.append(String.format("• %s (ID: %d, Subject: %s)\n", 
                teacher.getName(), teacher.getId(), teacher.getSubject()));
        }
        teacherList.setText(sb.toString());
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(LABEL_FONT);
        field.setBorder(new LineBorder(new Color(180, 180, 180), 1));
        return field;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new RoundedBorder(6, color));
        btn.setPreferredSize(new Dimension(120, 35));
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

// ==================== UTILITY CLASSES ====================

/**
 * Custom rounded border class.
 */
class RoundedBorder extends AbstractBorder {
    private int radius;
    private Color color;

    public RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 5, 5, 5);
    }
}
