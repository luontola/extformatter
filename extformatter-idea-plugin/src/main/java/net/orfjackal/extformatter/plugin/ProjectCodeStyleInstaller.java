/*
 * External Code Formatter
 * Copyright (c) 2007-2009  Esko Luontola, www.orfjackal.net
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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.CodeStyleManager;
import net.orfjackal.extformatter.CodeFormatter;
import org.jetbrains.annotations.*;
import org.picocontainer.MutablePicoContainer;

/**
 * Switches a project's {@link CodeStyleManager} to a {@link CodeFormatter} and back.
 *
 * @author Esko Luontola
 * @since 2.12.2007
 */
public class ProjectCodeStyleInstaller {

    private static final String CODE_STYLE_MANAGER_KEY = CodeStyleManager.class.getName();
    private static final Logger LOG = Logger.getInstance(ProjectCodeStyleInstaller.class.getName());

    @NotNull private final Project project;

    public ProjectCodeStyleInstaller(@NotNull Project project) {
        this.project = project;
    }

    @NotNull
    public Project getProject() {
        return project;
    }

    public void changeFormatterTo(@Nullable CodeFormatter formatter) {
        uninstallCodeFormatter();
        if (formatter != null) {
            installCodeFormatter(formatter);
        }
    }

    private void installCodeFormatter(@NotNull CodeFormatter formatter) {
        CodeStyleManager manager = CodeStyleManager.getInstance(project);
        if (!(manager instanceof ExternalizedCodeStyleManager)) {
            registerCodeStyleManager(project, new ExternalizedCodeStyleManager(manager, formatter));
        }
    }

    private void uninstallCodeFormatter() {
        CodeStyleManager manager = CodeStyleManager.getInstance(project);
        while (manager instanceof ExternalizedCodeStyleManager) {
            manager = ((ExternalizedCodeStyleManager) manager).getTarget();
            registerCodeStyleManager(project, manager);
        }
    }

    private static void registerCodeStyleManager(@NotNull Project project, @NotNull CodeStyleManager manager) {
        LOG.info("Registering code style manager '" + manager + "' for project '" + project.getName() + "'");
        MutablePicoContainer container = (MutablePicoContainer) project.getPicoContainer();
        container.unregisterComponent(CODE_STYLE_MANAGER_KEY);
        container.registerComponentInstance(CODE_STYLE_MANAGER_KEY, manager);
    }

    /* NOTES: Relevant places in IDEA's code style architecture

   from com.intellij.codeInsight.actions.ReformatCodeProcessor:
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
}
