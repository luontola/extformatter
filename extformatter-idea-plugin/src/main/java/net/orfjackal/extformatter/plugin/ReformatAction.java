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

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.picocontainer.MutablePicoContainer;

import java.util.Arrays;

/**
 * @author Esko Luontola
 * @since 2.12.2007
 */
public class ReformatAction extends AnAction {

    // TODO: use com.intellij.openapi.actionSystem.ActionManager to wrap the 

    public void actionPerformed(AnActionEvent e) {
        printDebugData(e);

        // com.intellij.codeInsight.actions.ReformatCodeAction
        AnAction reformatCode = ActionManager.getInstance().getAction("ReformatCode");
        System.out.println("reformatCode = " + reformatCode);

        VirtualFile[] selection = selectedFiles(e);
        if (ExternalizedCodeStyleManager.canReformat(selection)) {
            System.out.println("CAN REFORMAT");
        }

        Project project = DataKeys.PROJECT.getData(e.getDataContext());
        assert project != null;
        ProjectCodeStyleInstaller.installExternalCodeFormatter(project);
    }

    public void update(AnActionEvent e) {
        boolean enabled = ExternalizedCodeStyleManager.canReformat(selectedFiles(e));
        e.getPresentation().setEnabled(enabled);
    }

    private static VirtualFile[] selectedFiles(AnActionEvent e) {
        VirtualFile[] selected = DataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());
        if (selected == null) {
            selected = new VirtualFile[0];
        }
        return selected;
    }

    private static void printDebugData(AnActionEvent e) {
        System.out.println("------------- begin -------------");
        VirtualFile[] selection = selectedFiles(e);
        DataContext context = e.getDataContext();
        System.out.println("ReformatAction.actionPerformed");
        System.out.println(e);
        System.out.println("e.getPlace() = " + e.getPlace());
        System.out.println("e.getDataContext() = " + context);
        System.out.println("DataKeys.VIRTUAL_FILE = " + DataKeys.VIRTUAL_FILE.getData(context));
        System.out.println("DataKeys.VIRTUAL_FILE_ARRAY = " + Arrays.toString(DataKeys.VIRTUAL_FILE_ARRAY.getData(context)));

        System.out.println("canReformat(file) = " + ExternalizedCodeStyleManager.canReformat(selection));

        for (VirtualFile file : selection) {
            System.out.println("-----");
            System.out.println("file.getExtension() = " + file.getExtension());
            System.out.println("file.getFileSystem() = " + file.getFileSystem());
            System.out.println("file.getFileType() = " + file.getFileType());
            System.out.println("file.getLength() = " + file.getLength());
            System.out.println("file.getName() = " + file.getName());
            System.out.println("file.getParent() = " + file.getParent());
            System.out.println("file.getPath() = " + file.getPath());
            System.out.println("file.getPresentableName() = " + file.getPresentableName());
            System.out.println("file.isDirectory() = " + file.isDirectory());
            System.out.println("file.isInLocalFileSystem() = " + file.isInLocalFileSystem());
            System.out.println("file.isValid() = " + file.isValid());
            System.out.println("file.isWritable() = " + file.isWritable());
        }

        Project project = DataKeys.PROJECT.getData(e.getDataContext());
        assert project != null;
        MutablePicoContainer container = (MutablePicoContainer) project.getPicoContainer();
        System.out.println("project.getPicoContainer() = " + container);

        final String KEY = CodeStyleManager.class.getName();
        CodeStyleManager service = (CodeStyleManager) container.getComponentInstance(KEY);
        System.out.println("service = " + service);
        System.out.println("service.getClass() = " + service.getClass());
//        System.out.println("container.unregisterComponent(KEY); = " + container.unregisterComponent(KEY));
//        container.registerComponentInstance(KEY, "FooBar");
//        System.out.println("service (fake) = " + container.getComponentInstance(KEY));
//        System.out.println("container.unregisterComponent(KEY); = " + container.unregisterComponent(KEY));
//        container.registerComponentInstance(KEY, service);
//        System.out.println("service (back) = " + container.getComponentInstance(KEY));
        System.out.println("------------- end -------------");
    }
}
