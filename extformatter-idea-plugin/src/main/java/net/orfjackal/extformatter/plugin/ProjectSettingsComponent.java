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

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import net.orfjackal.extformatter.settings.Settings;
import net.orfjackal.extformatter.settings.SettingsManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
@State(
        name = "ExternalCodeFormatter",
        storages = {@Storage(
                id = "other",
                file = "$PROJECT_FILE$"
        )}
)
public class ProjectSettingsComponent implements ProjectComponent, Configurable, PersistentStateComponent<Settings> {

    @NotNull private final ProjectCodeStyleInstaller project;

    @NotNull private final Settings settings = new Settings();
    @Nullable private ProjectSettingsForm form;

    public ProjectSettingsComponent(@NotNull Project project) {
        this.project = new ProjectCodeStyleInstaller(project);
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "ProjectSettingsComponent";
    }

    public void projectOpened() {
        applySettings();
    }

    public void projectClosed() {
        uninstall();
    }

    private void applySettings() {
        project.changeFormatterTo(SettingsManager.newFormatter(settings));
    }

    private void uninstall() {
        project.changeFormatterTo(null);
    }

    @Nls
    public String getDisplayName() {
        return "External\nCode Formatter";
    }

    @Nullable
    public Icon getIcon() {
        return null; // TODO: add icon
    }

    @Nullable
    @NonNls
    public String getHelpTopic() {
        return null; // TODO: add help
    }

    @NotNull
    public JComponent createComponent() {
        if (form == null) {
            form = new ProjectSettingsForm();
        }
        return form.getRootComponent();
    }

    public boolean isModified() {
        return form != null && form.isModified(settings);
    }

    public void apply() throws ConfigurationException {
        if (form != null) {
            form.getData(settings);
            applySettings();
        }
    }

    public void reset() {
        if (form != null) {
            form.setData(settings);
        }
    }

    public void disposeUIResources() {
        form = null;
    }

    public Settings getState() {
        return settings.clone();
    }

    public void loadState(Settings state) {
        XmlSerializerUtil.copyBean(state, settings);
        applySettings();
    }
}
