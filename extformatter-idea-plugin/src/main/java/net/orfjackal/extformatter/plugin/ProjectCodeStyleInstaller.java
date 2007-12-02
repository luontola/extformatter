/*
 * External Code Formatter
 * Copyright (c) 2007 Esko Luontola, www.orfjackal.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.orfjackal.extformatter.plugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.source.codeStyle.CodeStyleManagerEx;
import net.orfjackal.extformatter.EclipseCodeFormatter;
import org.jetbrains.annotations.NotNull;
import org.picocontainer.MutablePicoContainer;

import java.io.File;

/**
 * @author Esko Luontola
 * @since 2.12.2007
 */
public class ProjectCodeStyleInstaller implements ProjectComponent {

    private static final String CODE_STYLE_MANAGER_KEY = CodeStyleManager.class.getName();

    @NotNull private final Project project;

    public ProjectCodeStyleInstaller(@NotNull Project project) {
        this.project = project;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "ProjectCodeStyleInstaller";
    }

    public void projectOpened() {
        installExternalCodeFormatter(project);
    }

    public void projectClosed() {
        uninstallExternalCodeFormatter(project);
    }

    /* NOTES:

    from ReformatCodeProcessor:
        CodeStyleManager.getInstance(myProject).reformatText(file, k.getStartOffset(), k.getEndOffset());else
        CodeStyleManager.getInstance(myProject).reformatText(file, 0, file.getTextRange().getEndOffset());
    - try to inject a custom com.intellij.psi.codeStyle.CodeStyleManager and replace it after the command exits

    from com.intellij.psi.codeStyle.CodeStyleManager:
        public static CodeStyleManager getInstance(@NotNull Project project) {
            return ServiceManager.getService(project, CodeStyleManager.class);
        }
    from com.intellij.openapi.components.ServiceManager
        public static <T> T getService(Project project, Class<T> serviceClass) {
            return (T)project.getPicoContainer().getComponentInstance(serviceClass.getName());
        }

     */

    public static void installExternalCodeFormatter(@NotNull Project project) {
        CodeStyleManagerEx manager = (CodeStyleManagerEx) CodeStyleManager.getInstance(project);
        if (!(manager instanceof DelegatingCodeStyleManager)) {
//            registerCodeStyleManager(project, new DelegatingCodeStyleManager(manager));
            // TODO: get formatter from some configuration manager
            // TODO: reinstall formatter when configuration is changed
            registerCodeStyleManager(project, new ExternalizedCodeStyleManager(manager,
                    new EclipseCodeFormatter(
                            new File("C:\\eclipse-SDK-3.3.1-win32\\eclipse\\eclipsec.exe"),
                            new File("C:\\eclipse-SDK-3.3.1-win32\\workspace\\foo\\.settings\\org.eclipse.jdt.core.prefs"))));
        }
    }

    public static void uninstallExternalCodeFormatter(@NotNull Project project) {
        CodeStyleManagerEx manager = (CodeStyleManagerEx) CodeStyleManager.getInstance(project);
        if (manager instanceof DelegatingCodeStyleManager) {
            while (manager instanceof DelegatingCodeStyleManager) {
                manager = ((DelegatingCodeStyleManager) manager).getTarget();
            }
            registerCodeStyleManager(project, manager);
        }
    }

    private static void registerCodeStyleManager(Project project, CodeStyleManagerEx codeStyleManager) {
        MutablePicoContainer container = (MutablePicoContainer) project.getPicoContainer();
        container.unregisterComponent(CODE_STYLE_MANAGER_KEY);
        container.registerComponentInstance(CODE_STYLE_MANAGER_KEY, codeStyleManager);
    }
}
