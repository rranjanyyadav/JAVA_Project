import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * UI for entering student details. Called by teachers after login.
 */
public class StudentEntryUI {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color ACCENT_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    private StudentManager studentManager;
    private JTextArea studentList;

    /**
     * Creates the student entry UI dialog.
     * @param studentManager the student manager
     * @param parent the parent frame
     */
    public StudentEntryUI(StudentManager studentManager, JFrame parent) {
        this.studentManager = studentManager;
        createUI(parent);
    }

    /**
     * Creates and displays the UI as a dialog.
     */
    private void createUI(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Add Students", true);
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

        JLabel titleLabel = new JLabel("Add Student Details");
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
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(LABEL_FONT);
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField idField = createStyledTextField();
        formPanel.add(idField, gbc);

        // Semester field
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

        // Button panel
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

        // Students list panel
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

        // Initial display
        updateStudentListDisplay();
    }

    /**
     * Adds a student from the form fields.
     */
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

    /**
     * Updates the student list display.
     */
    private void updateStudentListDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Student student : studentManager.getAllStudents()) {
            sb.append(String.format("• %s (ID: %d, Sem: %d)\n", 
                student.getName(), student.getId(), student.getSemester()));
        }
        studentList.setText(sb.toString());
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
