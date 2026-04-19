import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern Teacher Dashboard UI for updating student information.
 */
public class TeacherDashboard {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 450;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color BUTTON_COLOR = new Color(155, 89, 182);
    
    private AuthManager authManager;
    private StudentManager studentManager;
    private TeacherManager teacherManager;

    /**
     * Creates the teacher dashboard window.
     * @param manager the student manager
     * @param authManager the auth manager
     * @param studentManager the student manager
     * @param teacherManager the teacher manager
     */
    public TeacherDashboard(StudentManager manager, AuthManager authManager,
                           StudentManager studentManager, TeacherManager teacherManager) {
        this.authManager = authManager;
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
        JFrame frame = new JFrame("Teacher Dashboard");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        JLabel titleLabel = new JLabel("Update Student ID");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
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

        // Current Student ID
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Current Student ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        idLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(idLabel, gbc);

        gbc.gridy = 1;
        JTextField idField = createStyledTextField();
        formPanel.add(idField, gbc);

        // New Student ID
        gbc.gridy = 2;
        JLabel newIdLabel = new JLabel("New Student ID:");
        newIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        newIdLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(newIdLabel, gbc);

        gbc.gridy = 3;
        JTextField newIdField = createStyledTextField();
        formPanel.add(newIdField, gbc);

        // Button panel
        gbc.gridy = 4;
        gbc.insets = new Insets(25, 0, 15, 0);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton updateBtn = createStyledButton("Update Student", BUTTON_COLOR);
        updateBtn.addActionListener(e -> handleUpdate(frame, idField, newIdField, manager));

        JButton addStudentsBtn = createStyledButton("Add Students", new Color(46, 204, 113));
        addStudentsBtn.addActionListener(e -> new StudentEntryUI(manager, frame));

        JButton logoutBtn = createStyledButton("Logout", new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginUI(authManager, studentManager, teacherManager);
        });

        buttonPanel.add(updateBtn);
        buttonPanel.add(addStudentsBtn);
        buttonPanel.add(logoutBtn);
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Handles the student ID update process.
     */
    private void handleUpdate(JFrame frame, JTextField idField, JTextField newIdField, StudentManager manager) {
        try {
            String idText = idField.getText().trim();
            String newIdText = newIdField.getText().trim();

            if (idText.isEmpty() || newIdText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idText);
            int newId = Integer.parseInt(newIdText);

            if (id == newId) {
                JOptionPane.showMessageDialog(frame, "New ID must be different from current ID", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            manager.updateById(id, newId);
            JOptionPane.showMessageDialog(frame, "Student ID updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            idField.setText("");
            newIdField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numeric IDs", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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