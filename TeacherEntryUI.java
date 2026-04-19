import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * UI for entering teacher details. Called by admins after login.
 */
public class TeacherEntryUI {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color ACCENT_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private TeacherManager teacherManager;
    private AuthManager authManager;
    private JTextArea teacherList;

    /**
     * Creates the teacher entry UI dialog.
     * @param teacherManager the teacher manager
     * @param authManager the auth manager
     * @param parent the parent frame
     */
    public TeacherEntryUI(TeacherManager teacherManager, AuthManager authManager, JFrame parent) {
        this.teacherManager = teacherManager;
        this.authManager = authManager;
        createUI(parent);
    }

    /**
     * Creates and displays the UI as a dialog.
     */
    private void createUI(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Add Teachers", true);
        dialog.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Main panel with background color
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ACCENT_COLOR);
        headerPanel.setBorder(new RoundedBorder(10, ACCENT_COLOR));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titleLabel = new JLabel("Add Teacher Details");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new RoundedBorder(8, new Color(200, 200, 200)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
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

        // ID field
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

        // Subject field
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

        // Username field for login
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

        // Password field for login
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

        // Button panel
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

        // Teachers list panel
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

    /**
     * Adds a teacher from the form fields.
     */
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

            // Add teacher
            Teacher teacher = new Teacher(name, id, subject);
            teacherManager.addTeacher(teacher);

            // Add user account for login
            User teacherUser = new User(username, password, "teacher");
            authManager.addUser(teacherUser);

            updateTeacherListDisplay();
            JOptionPane.showMessageDialog(dialog, "Teacher added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Teacher ID must be a number", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates the teacher list display.
     */
    private void updateTeacherListDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Teacher teacher : teacherManager.getAllTeachers()) {
            sb.append(String.format("• %s (ID: %d, Subject: %s)\n", 
                teacher.getName(), teacher.getId(), teacher.getSubject()));
        }
        teacherList.setText(sb.toString());
    }

    /**
     * Creates a styled text field.
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(LABEL_FONT);
        field.setBorder(new LineBorder(new Color(180, 180, 180), 1));
        return field;
    }

    /**
     * Creates a styled button.
     */
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new RoundedBorder(6, color));
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }

    /**
     * Custom rounded border class.
     */
    private static class RoundedBorder extends AbstractBorder {
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
}
