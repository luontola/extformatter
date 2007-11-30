@echo off
call mvn -o clean package assembly:assembly
echo.
pause
