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
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color BUTTON_COLOR = new Color(46, 204, 113);

    private StudentManager studentManager;
    private TeacherManager teacherManager;
    private AuthManager authManager;

    public LoginUI(AuthManager auth, StudentManager studentManager, TeacherManager teacherManager) {
        this.authManager = auth;
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
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
 * Modern Admin Dashboard UI for administrative functions.
 */
class AdminDashboard {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color ACCENT_COLOR = new Color(230, 126, 34);
    private static final Color BUTTON_COLOR = new Color(52, 152, 219);
    
    private TeacherManager teacherManager;
    private AuthManager authManager;
    private StudentManager studentManager;

    public AdminDashboard(TeacherManager teacherManager, AuthManager authManager, StudentManager studentManager) {
        this.teacherManager = teacherManager;
        this.authManager = authManager;
        this.studentManager = studentManager;
        
        JFrame frame = new JFrame("Admin Dashboard");
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

        JLabel titleLabel = new JLabel("Admin Panel");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        welcomePanel.add(Box.createVerticalStrut(20));

        JLabel welcomeLabel = new JLabel("Welcome to Admin Panel");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(welcomeLabel);

        welcomePanel.add(Box.createVerticalStrut(15));

        JLabel descLabel = new JLabel("Administrative Features:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        descLabel.setForeground(ACCENT_COLOR);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(descLabel);

        welcomePanel.add(Box.createVerticalStrut(15));

        String[] features = {
            "• Add and manage teachers",
            "• View and manage student records",
            "• Monitor teacher activities",
            "• Generate system reports",
            "• Configure system settings"
        };

        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            featureLabel.setForeground(new Color(52, 73, 94));
            featureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            welcomePanel.add(featureLabel);
        }

        welcomePanel.add(Box.createVerticalStrut(20));

        JButton addTeachersBtn = createStyledButton("Add Teachers", BUTTON_COLOR);
        addTeachersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTeachersBtn.addActionListener(e -> new TeacherEntryUI(teacherManager, authManager, frame));
        welcomePanel.add(addTeachersBtn);

        welcomePanel.add(Box.createVerticalStrut(10));

        JButton logoutBtn = createStyledButton("Logout", new Color(231, 76, 60));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI(authManager, studentManager, teacherManager);
        });
        welcomePanel.add(logoutBtn);

        welcomePanel.add(Box.createVerticalStrut(10));

        contentPanel.add(welcomePanel);
        contentPanel.add(Box.createVerticalGlue());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        btn.setPreferredSize(new Dimension(150, 35));
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
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color BUTTON_COLOR = new Color(52, 152, 219);
    
    private AuthManager authManager;
    private StudentManager studentManager;
    private TeacherManager teacherManager;

    public StudentDashboard(StudentManager manager, AuthManager authManager, 
                           StudentManager studentManager, TeacherManager teacherManager) {
        this.authManager = authManager;
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
        
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

        JButton loadBtn = createStyledButton("Load Students", BUTTON_COLOR);
        loadBtn.addActionListener(e -> loadStudentsToTable(model, manager));

        JButton logoutBtn = createStyledButton("Logout", new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI(authManager, studentManager, teacherManager);
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
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color BUTTON_COLOR = new Color(155, 89, 182);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    
    private AuthManager authManager;
    private StudentManager studentManager;
    private TeacherManager teacherManager;
    private Student currentStudent;

    public TeacherDashboard(StudentManager manager, AuthManager authManager,
                           StudentManager studentManager, TeacherManager teacherManager) {
        this.authManager = authManager;
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
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

        JButton clearBtn = createStyledButton("Clear", new Color(189, 195, 199));
        clearBtn.addActionListener(e -> clearFields(searchIdField, nameField, currentIdField, semesterField, newNameField, newIdField, newSemesterField));

        actionButtonPanel.add(updateBtn);
        actionButtonPanel.add(clearBtn);
        scrollablePanel.add(actionButtonPanel, gbc);

        // Add Students Button
        gbc.gridy = 18;
        gbc.insets = new Insets(10, 0, 10, 0);
        JButton addStudentsBtn = createStyledButton("Add Students", SUCCESS_COLOR);
        addStudentsBtn.addActionListener(e -> new StudentEntryUI(manager, frame));
        scrollablePanel.add(addStudentsBtn, gbc);

        // Logout Button
        gbc.gridy = 19;
        JButton logoutBtn = createStyledButton("Logout", new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI(authManager, studentManager, teacherManager);
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
    private static final Color ACCENT_COLOR = new Color(41, 128, 185);
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

        JButton addBtn = createStyledButton("Add Student", ACCENT_COLOR);
        addBtn.addActionListener(e -> {
            addStudent(dialog, nameField, idField, semesterField);
            nameField.setText("");
            idField.setText("");
            semesterField.setText("");
            nameField.requestFocus();
        });

        JButton backBtn = createStyledButton("Back", new Color(149, 165, 166));
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
    private static final Color ACCENT_COLOR = new Color(41, 128, 185);
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

        JButton addBtn = createStyledButton("Add Teacher", ACCENT_COLOR);
        addBtn.addActionListener(e -> {
            addTeacher(dialog, nameField, idField, subjectField, usernameField, passwordField);
            nameField.setText("");
            idField.setText("");
            subjectField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            nameField.requestFocus();
        });

        JButton backBtn = createStyledButton("Back", new Color(149, 165, 166));
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
