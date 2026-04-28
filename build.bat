@echo off
REM =============================================================
REM Student Management System Build Script
REM This script compiles and creates a JAR file for packaging
REM =============================================================

echo.
echo ========================================
echo SMS Build Script
echo ========================================
echo.

REM Compile Java files
echo [1/3] Compiling Java files...
javac -cp ".;sqlite-jdbc.jar;slf4j-api.jar;slf4j-simple.jar" SMS_Consolidated.java LoginUI_Consolidated.java
if errorlevel 1 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)
echo [✓] Compilation successful

echo.
echo [2/3] Creating JAR file...
REM Create JAR file with manifest and all class files
jar cfm SMS_App.jar manifest.txt ^
    SMS_Consolidated.class ^
    LoginUI.class AdminDashboard.class StudentDashboard.class TeacherDashboard.class ^
    StudentEntryUI.class TeacherEntryUI.class RoundedBorder.class ^
    User.class Student.class Teacher.class ^
    DatabaseHelper.class AuthManager.class StudentManager.class TeacherManager.class

if errorlevel 1 (
    echo ERROR: JAR creation failed!
    pause
    exit /b 1
)
echo [✓] JAR file created: SMS_App.jar

echo.
echo [3/3] Copying dependencies...
REM Dependencies are already in the directory, just verify they exist
if not exist sqlite-jdbc.jar (
    echo ERROR: sqlite-jdbc.jar not found!
    pause
    exit /b 1
)
if not exist slf4j-api.jar (
    echo ERROR: slf4j-api.jar not found!
    pause
    exit /b 1
)
if not exist slf4j-simple.jar (
    echo ERROR: slf4j-simple.jar not found!
    pause
    exit /b 1
)
echo [✓] All dependencies verified

echo.
echo ========================================
echo Build Complete!
echo ========================================
echo.
echo JAR File: SMS_App.jar
echo.
echo Next Steps:
echo 1. Download Launch4j from: https://launch4j.sourceforge.net/
echo 2. Install Launch4j
echo 3. Open Launch4j and create new configuration:
echo    - Output: SMS_App.exe
echo    - Jar: SMS_App.jar (from this directory)
echo    - Main class: SMS_Consolidated
echo    - Include all JARs: sqlite-jdbc.jar, slf4j-api.jar, slf4j-simple.jar
echo 4. Build the executable
echo.
echo For detailed instructions, see LAUNCH4J_SETUP.txt
echo.
pause
