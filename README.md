# Student Management System (SMS)

A modern Java Swing application for managing student and teacher information with role-based access control and persistent SQLite database storage.

## Features

- **Role-Based Access Control**: Separate interfaces for Students, Teachers, and Admins
- **User Authentication**: Secure login with credentials stored in SQLite database
- **Data Persistence**: All data persists across application restarts
- **Modern UI**: Professional Swing interface with custom styling and responsive design
- **Student Management**: Teachers can add and update student records
- **Teacher Management**: Admins can add teachers and create their login accounts
- **Logout & Navigation**: Logout buttons on all dashboards, back buttons on entry dialogs

## Architecture

### Core Components

**Managers:**
- `AuthManager.java` - User authentication and credential management
- `StudentManager.java` - Student data with database persistence
- `TeacherManager.java` - Teacher data with database persistence
- `DatabaseHelper.java` - SQLite JDBC connection and database operations

**User Interface:**
- `LoginUI.java` - Authentication interface with role-based routing
- `StudentDashboard.java` - Displays student information
- `TeacherDashboard.java` - Interface for student management
- `AdminDashboard.java` - Administrative functions for teacher management
- `StudentEntryUI.java` - Dialog for adding students
- `TeacherEntryUI.java` - Dialog for adding teachers and creating accounts

**Data Models:**
- `User.java` - User credentials and role
- `Student.java` - Student information
- `Teacher.java` - Teacher information

**Entry Point:**
- `SMS.java` - Main application launcher

### Database Schema

**users table**
```sql
CREATE TABLE users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username TEXT UNIQUE NOT NULL,
  password TEXT NOT NULL,
  role TEXT NOT NULL
)
```

**students table**
```sql
CREATE TABLE students (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  semester INTEGER NOT NULL
)
```

**teachers table**
```sql
CREATE TABLE teachers (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  subject TEXT NOT NULL
)
```

## Getting Started

### Prerequisites

- Java 11 or higher
- Python 3.8+ (for graphify analysis, optional)

### Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/student-management-system.git
cd student-management-system
```

2. Download SQLite JDBC driver dependencies:
```bash
# Already included in repository: sqlite-jdbc.jar, slf4j-api.jar, slf4j-simple.jar
```

3. Compile the project
```bash
javac -cp ".;sqlite-jdbc.jar;slf4j-api.jar;slf4j-simple.jar" *.java
```

### Running the Application

**Windows (using batch file):**
```bash
run.bat
```

**Windows (command line):**
```bash
java -cp ".;sqlite-jdbc.jar;slf4j-api.jar;slf4j-simple.jar" SMS
```

**Mac/Linux:**
```bash
java -cp ".:sqlite-jdbc.jar:slf4j-api.jar:slf4j-simple.jar" SMS
```

## Demo Credentials

The application comes with default demo accounts:

| Username | Password | Role   |
|----------|----------|--------|
| student1 | 123      | Student|
| teacher1 | 123      | Teacher|
| admin1   | 123      | Admin  |

**First Time Usage:**
1. Login as `admin1/123`
2. Add teachers with custom usernames/passwords
3. Teachers can login and add students
4. Students can login and view information

## Workflow

### Admin Flow
1. Login as admin → Admin Dashboard
2. Click "Add Teachers" to create teacher accounts with login credentials
3. Logout to return to login screen

### Teacher Flow
1. Login with teacher credentials → Teacher Dashboard
2. Click "Add Students" to add student records
3. Click "Update Student" to modify student ID
4. Logout to return to login screen

### Student Flow
1. Login with student credentials → Student Dashboard
2. View student information in table
3. Click "Load Students" to refresh data
4. Logout to return to login screen

## Project Structure

```
student-management-system/
├── *.java              # 9 Java source files
├── *.class            # Compiled class files
├── sms_database.db    # SQLite database (auto-created on first run)
├── run.bat            # Windows batch file for easy launching
├── .gitignore         # Git ignore patterns
├── README.md          # This file
└── graphify-out/      # Knowledge graph analysis (auto-generated)
    ├── graph.json
    ├── GRAPH_REPORT.md
    └── cache/
```

## Technology Stack

- **Language**: Java
- **GUI Framework**: Java Swing
- **Database**: SQLite with JDBC driver
- **Logging**: SLF4J
- **Build**: Javac compiler
- **Version Control**: Git

## Features in Detail

### Authentication
- Credentials stored securely in SQLite database
- Parameterized SQL queries to prevent injection
- Role-based routing after login

### Database Persistence
- Uses SQLite for lightweight, file-based storage
- Graceful fallback if database unavailable
- Automatic table creation on first run
- Prepared statements for all database operations

### User Interface
- Professional color scheme (Blue primary, Green accents)
- Rounded borders and hover effects
- Modal dialogs for data entry
- Responsive tables with proper formatting
- Back buttons on all entry dialogs
- Logout buttons on all dashboards

### Data Management
- Add students/teachers through dialogs
- Update student IDs through teacher dashboard
- View all records in formatted tables
- Data persists across application restarts

## Development

### Compilation
```bash
javac -cp ".;sqlite-jdbc.jar;slf4j-api.jar;slf4j-simple.jar" *.java
```

### Code Analysis (Optional)
```bash
python graphify_final.py  # Generate architecture graph
```

## Future Enhancements

- [ ] Search and filter functionality
- [ ] Edit existing records
- [ ] Delete student/teacher accounts
- [ ] Password change functionality
- [ ] Admin user management
- [ ] Export to CSV/PDF
- [ ] Multiple database support
- [ ] Web-based version

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Author

Created as a Student Management System with focus on clean architecture, persistent data storage, and user-friendly interface.

## Support

For issues, questions, or suggestions, please open an issue on GitHub.
