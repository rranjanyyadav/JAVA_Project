# Student Management System - Architecture Report

## Overview
- Files: 15 Java classes
- Components: 111 classes, methods, and types
- Dependencies: 225 relationships

## Architecture

### Core Managers
- AuthManager: User authentication and credential management
- StudentManager: Student data persistence and management  
- TeacherManager: Teacher data and subject management
- DatabaseHelper: SQLite JDBC database operations

### User Interface Components
- LoginUI: Authentication and role-based routing
- StudentDashboard: Student information display
- TeacherDashboard: Student management interface
- AdminDashboard: Teacher and system administration
- StudentEntryUI: Student data entry dialog
- TeacherEntryUI: Teacher and account creation dialog

### Data Models
- User: Authentication credentials and roles
- Student: Student information (name, ID, semester)
- Teacher: Teacher information (name, ID, subject)

### Entry Point
- SMS: Main application launcher with database initialization

## Key Dependencies
- SQLite (JDBC) for persistent storage
- Java Swing for GUI
- Role-based access control (Student, Teacher, Admin)

## Database Schema
- users: username, password, role
- students: id, name, semester
- teachers: id, name, subject

## Features
[X] Database persistence
[X] User authentication with roles
[X] Logout functionality
[X] Entry dialog with back buttons
[X] Student/teacher management workflow
