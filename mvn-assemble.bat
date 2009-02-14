@echo off
call mvn clean package assembly:assembly
echo.
pause
