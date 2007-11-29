
    ABOUT

External Code Formatter is a plugin for IntelliJ IDEA which allows
using an external code formatter, such as the one Eclipse has, from
within IDEA.


    BUILDING

1. Configure IDEA_HOME in "idea-install-files.bat" and run the file
   to add the necessary jars to your local maven repository. You need
   to do this only once.

2. Run the following command
        mvn clean package assembly:assembly

3. Collect the *-bin.zip and *-src.zip files from /target directory.


    LICENSE

External Code Formatter
Copyright (c) 2007 Esko Luontola, www.orfjackal.net

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
