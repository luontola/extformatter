rem complete your IDEA Home folder here
set IDEA_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 7.0.1
set IDEA_VERSION=7.0.1.7364
echo %IDEA_HOME%
call mvn install:install-file -DgroupId=com.intellij.idea -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\openapi.jar" -DartifactId=openapi -Dversion=%IDEA_VERSION%
call mvn install:install-file -DgroupId=com.intellij.idea -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\annotations.jar" -DartifactId=annotations -Dversion=%IDEA_VERSION%
call mvn install:install-file -DgroupId=com.intellij.idea -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\extensions.jar" -DartifactId=extensions -Dversion=%IDEA_VERSION%
pause
