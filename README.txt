
    ABOUT

External Code Formatter is a plugin for IntelliJ IDEA which allows using a code 
formatter, such as the one Eclipse has, in addition to the one which is built 
into IDEA.


    INSTALLING

1. Unpack the binary release to %HOMEPATH%\.IntelliJIdea70\config\plugins
   or use IDEA's plugin manager to install it automatically.

2. Follow the instructions in the plugin's configuration dialog.


    BUILDING

1. You will need have Maven (http://maven.apache.org/), IntelliJ IDEA
   (http://www.jetbrains.com/idea/) and its Plugin Development Kit installed. 
   The IDEA version required reads in profiles.xml under the <idea.version> tag.
   Change <idea.home> in profiles.xml to point to the IDEA installation.

2. Run the command "mvn clean package assembly:assembly" in the project's root
   source directory. On successive calls you may use the "mvn-assemble.bat" 
   script which operates in offline mode.

3. Collect the *-bin.zip and *-src.zip files from the /target directory.


    RELEASING A NEW VERSION

1. Commands for releasing a new version:

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
Copyright (c) 2007 Esko Luontola, www.orfjackal.net

Licensed under the Apache License, Version 2.0 (the "License"); you may not use 
this file except in compliance with the License. You may obtain a copy of the 
License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed 
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
CONDITIONS OF ANY KIND, either express or implied. See the License for the 
specific language governing permissions and limitations under the License.
