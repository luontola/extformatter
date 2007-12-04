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

import javax.swing.*;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class ProjectSettingsForm {

    private JPanel rootComponent;

    private JCheckBox pluginEnabledCheckBox;
    private JTextField eclipseExecutableField;
    private JTextField eclipsePrefsField;

    private JButton locateExecutableButton;
    private JButton locatePrefsButton;

    public JPanel getRootComponent() {
        return rootComponent;
    }

    public void setData(ProjectSettings data) {
        eclipseExecutableField.setText(data.getEclipseExecutable());
        eclipsePrefsField.setText(data.getEclipsePrefs());
        pluginEnabledCheckBox.setSelected(data.isPluginEnabled());
    }

    public void getData(ProjectSettings data) {
        data.setEclipseExecutable(eclipseExecutableField.getText());
        data.setEclipsePrefs(eclipsePrefsField.getText());
        data.setPluginEnabled(pluginEnabledCheckBox.isSelected());
    }

    @SuppressWarnings({"ConstantConditions", "RedundantIfStatement"})
    public boolean isModified(ProjectSettings data) {
        if (eclipseExecutableField.getText() != null ? !eclipseExecutableField.getText().equals(data.getEclipseExecutable()) : data.getEclipseExecutable() != null) {
            return true;
        }
        if (eclipsePrefsField.getText() != null ? !eclipsePrefsField.getText().equals(data.getEclipsePrefs()) : data.getEclipsePrefs() != null) {
            return true;
        }
        if (pluginEnabledCheckBox.isSelected() != data.isPluginEnabled()) {
            return true;
        }
        return false;
    }
}
