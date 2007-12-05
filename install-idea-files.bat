@echo off
rem complete your IDEA Home folder here
set IDEA_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 7.0.1
set IDEA_VERSION=7.0.1.7364
echo.
echo Installing packages of IDEA version %IDEA_VERSION% from "%IDEA_HOME%" to the local Maven repository...
echo.
call mvn install:install-file -DgroupId=com.intellij.idea -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\openapi.jar" -DartifactId=openapi -Dversion=%IDEA_VERSION%
call mvn install:install-file -DgroupId=com.intellij.idea -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\annotations.jar" -DartifactId=annotations -Dversion=%IDEA_VERSION%
call mvn install:install-file -DgroupId=com.intellij.idea -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\extensions.jar" -DartifactId=extensions -Dversion=%IDEA_VERSION%
call mvn install:install-file -DgroupId=com.intellij.idea -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\forms_rt.jar" -DartifactId=forms_rt -Dversion=%IDEA_VERSION%
echo.
pause
