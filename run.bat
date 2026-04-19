@echo off
title Student Management System
cd /d "%~dp0"
java -cp ".;sqlite-jdbc.jar;slf4j-api.jar;slf4j-simple.jar" SMS
pause
