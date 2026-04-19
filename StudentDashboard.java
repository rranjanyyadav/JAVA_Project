import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern Student Dashboard UI for viewing student information.
 */
public class StudentDashboard {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final String[] COLUMN_NAMES = {"Name", "ID", "Semester"};
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color BUTTON_COLOR = new Color(52, 152, 219);
    
    private AuthManager authManager;
    private StudentManager studentManager;
    private TeacherManager teacherManager;

    /**
     * Creates the student dashboard window.
     * @param manager the student manager
     * @param authManager the auth manager
     * @param studentManager the student manager
     * @param teacherManager the teacher manager
     */
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

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));

        JLabel titleLabel = new JLabel("Student Records");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create table model
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
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        // Center align table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
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

        // Auto-load students on startup
        loadStudentsToTable(model, manager);
    }

    /**
     * Loads students from the manager into the table.
     */
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

    /**
     * Creates a styled button.
     */
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