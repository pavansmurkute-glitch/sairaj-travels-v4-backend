@echo off
echo Starting Sairaj Travels Backend...
echo.

REM Set the profile to local
set SPRING_PROFILES_ACTIVE=local

REM Run the Spring Boot application
mvn spring-boot:run

pause
