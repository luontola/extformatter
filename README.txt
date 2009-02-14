
    ABOUT

External Code Formatter is a plugin for IntelliJ IDEA which allows using a code 
formatter, such as the one Eclipse has, in addition to the one which is built 
into IDEA.


    INSTALLING

1. Unpack the binary release to %HOMEPATH%\.IntelliJIdea80\config\plugins
   or use IDEA's plugin manager to install it automatically.

2. Follow the instructions in the plugin's configuration dialog and help pages.


    BUILDING

1. You will need have Maven (http://maven.apache.org/), IntelliJ IDEA
   (http://www.jetbrains.com/idea/) and its Plugin Development Kit installed. 
   The IDEA version required reads in profiles.xml under the <idea.version> tag.

2. Change <idea.home> in profiles.xml to point to the IDEA install location.

3. Run the command "mvn clean package" in the project's root source directory. 
   You may also use the "mvn-assemble.bat" script.

4. Collect the ZIP files from /target and /extformatter-dist/target directories.


    RELEASING A NEW VERSION

1. Run these commands in the project's root directory to release a new version:

      svn update
      mvn clean
      mvn release:prepare
      mvn release:clean

2. Afterwards, export the new tag from SVN and build the artifacts as explained 
   in the preceding section.


    CODE DOCUMENTATION

When you build the project with Maven, a more readable plain text summary of the 
tests will be generated in each module's "/target/jdave" directory. Start your 
reading from there. For more details and usage examples, read the *Spec.java 
files in the "/src/test/java" directories.


    LICENSE

External Code Formatter
Copyright (c) 2007-2009  Esko Luontola, www.orfjackal.net

Licensed under the Apache License, Version 2.0 (the "License"); you may not use 
this file except in compliance with the License. You may obtain a copy of the 
License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed 
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
CONDITIONS OF ANY KIND, either express or implied. See the License for the 
specific language governing permissions and limitations under the License.
