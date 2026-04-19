import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern Admin Dashboard UI for administrative functions.
 */
public class AdminDashboard {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color ACCENT_COLOR = new Color(230, 126, 34);
    private static final Color BUTTON_COLOR = new Color(52, 152, 219);
    
    private TeacherManager teacherManager;
    private AuthManager authManager;
    private StudentManager studentManager;

    /**
     * Creates the admin dashboard window.
     * @param teacherManager the teacher manager
     * @param authManager the auth manager
     * @param studentManager the student manager
     */
    public AdminDashboard(TeacherManager teacherManager, AuthManager authManager, StudentManager studentManager) {
        this.teacherManager = teacherManager;
        this.authManager = authManager;
        this.studentManager = studentManager;
        JFrame frame = new JFrame("Admin Dashboard");
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

        JLabel titleLabel = new JLabel("Admin Panel");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Content panel with icon and message
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Welcome section
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

        // Add button for teachers
        JButton addTeachersBtn = createStyledButton("Add Teachers", BUTTON_COLOR);
        addTeachersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTeachersBtn.addActionListener(e -> new TeacherEntryUI(teacherManager, authManager, frame));
        welcomePanel.add(addTeachersBtn);

        welcomePanel.add(Box.createVerticalStrut(10));

        // Logout button
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

    /**
     * Creates a styled button.
     */
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