@echo off
call mvn -o clean package assembly:assembly
call rmdir /s /q "%USERPROFILE%\.IntelliJIdea70\system\plugins-sandbox\plugins\ExternalCodeFormatter"
call rmdir /s /q "%USERPROFILE%\.IntelliJIdea70\system\plugins-sandbox\plugins\extformatter-idea-plugin"
call "C:\Program Files\7-Zip\7z.exe" x -y -o"%USERPROFILE%\.IntelliJIdea70\system\plugins-sandbox\plugins" target\extformatter-*-bin.zip
cd "C:\Program Files\JetBrains\IntelliJ IDEA 7.0.2\bin"
call "C:\Program Files\Java\jdk1.6.0_03\bin\java" -Xms32m -Xmx1024m -XX:MaxPermSize=200m -ea -server -Dsun.awt.keepWorkingSetOnMinimize=true -XX:SoftRefLRUPolicyMSPerMB=10000 "-Xbootclasspath/p:C:/Program Files/JetBrains/IntelliJ IDEA 7.0.2\lib\boot.jar" "-Didea.config.path=%USERPROFILE%\.IntelliJIdea70\system\plugins-sandbox\config" "-Didea.system.path=%USERPROFILE%\.IntelliJIdea70\system\plugins-sandbox\system" "-Didea.plugins.path=%USERPROFILE%\.IntelliJIdea70\system\plugins-sandbox\plugins" -Didea.launcher.port=7532 "-Didea.launcher.bin.path=C:\Program Files\JetBrains\IntelliJ IDEA 7576\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.6.0_03\lib\tools.jar;C:/Program Files/JetBrains/IntelliJ IDEA 7.0.2\lib\idea_rt.jar;C:/Program Files/JetBrains/IntelliJ IDEA 7.0.2\lib\idea.jar;C:/Program Files/JetBrains/IntelliJ IDEA 7.0.2\lib\bootstrap.jar;C:/Program Files/JetBrains/IntelliJ IDEA 7.0.2\lib\extensions.jar;C:/Program Files/JetBrains/IntelliJ IDEA 7.0.2\lib\openapi.jar;C:/Program Files/JetBrains/IntelliJ IDEA 7.0.2\lib\jdom.jar;C:/Program Files/JetBrains/IntelliJ IDEA 7.0.2\lib\log4j.jar;C:\Program Files\JetBrains\IntelliJ IDEA 7576\lib\idea_rt.jar" com.intellij.rt.execution.application.AppMain com.intellij.idea.Main
call rmdir /s /q "%USERPROFILE%\.IntelliJIdea70\system\plugins-sandbox\plugins\ExternalCodeFormatter"
echo.
rem pause
