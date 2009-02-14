@echo off
call mvn clean package assembly:assembly
call rmdir /s /q "%USERPROFILE%\.IntelliJIdea80\system\plugins-sandbox\plugins\extformatter"
call "C:\Program Files\7-Zip\7z.exe" x -y -o"%USERPROFILE%\.IntelliJIdea80\system\plugins-sandbox\plugins" target\extformatter-*-bin.zip
call "C:\Program Files\JetBrains\idea-8.0-jdk15\bin\idea.exe"
