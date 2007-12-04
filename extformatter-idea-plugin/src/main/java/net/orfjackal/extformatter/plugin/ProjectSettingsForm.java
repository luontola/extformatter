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

import net.orfjackal.extformatter.settings.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class ProjectSettingsForm {

    private JCheckBox pluginEnabled;
    private JTextField eclipseExecutable;
    private JTextField eclipsePrefs;

    private JPanel rootComponent;
    private JLabel titleLabel;
    private JButton eclipseExecutableBrowse;
    private JButton eclipsePrefsBrowse;
    private JLabel eclipseExecutableLabel;
    private JLabel eclipsePrefsLabel;

    public ProjectSettingsForm() {
        pluginEnabled.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateComponents();
            }
        });
        eclipseExecutableBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseForFile(eclipseExecutable);
            }
        });
        eclipsePrefsBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseForFile(eclipsePrefs);
            }
        });
        updateComponents();
    }

    private void updateComponents() {
        JComponent[] affectedByPluginEnabled = new JComponent[]{
                titleLabel,
                eclipseExecutable,
                eclipseExecutableBrowse,
                eclipseExecutableLabel,
                eclipsePrefs,
                eclipsePrefsBrowse,
                eclipsePrefsLabel,
        };
        for (JComponent component : affectedByPluginEnabled) {
            component.setEnabled(pluginEnabled.isSelected());
        }
    }

    private void browseForFile(JTextField target) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileHidingEnabled(false);    // Eclipse's prefs file is in a hidden ".settings" directory

        File currentSelection = new File(target.getText());
        chooser.setCurrentDirectory(currentSelection);
        chooser.setSelectedFile(currentSelection);

        int result = chooser.showOpenDialog(rootComponent);
        if (result == JFileChooser.APPROVE_OPTION) {
            target.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public JPanel getRootComponent() {
        return rootComponent;
    }

    public void setData(Settings data) {
        eclipseExecutable.setText(data.getEclipseExecutable());
        eclipsePrefs.setText(data.getEclipsePrefs());
        pluginEnabled.setSelected(data.isPluginEnabled());
        updateComponents();
    }

    public void getData(Settings data) {
        data.setEclipseExecutable(eclipseExecutable.getText());
        data.setEclipsePrefs(eclipsePrefs.getText());
        data.setPluginEnabled(pluginEnabled.isSelected());
    }

    @SuppressWarnings({"ConstantConditions", "RedundantIfStatement"})
    public boolean isModified(Settings data) {
        if (eclipseExecutable.getText() != null ? !eclipseExecutable.getText().equals(data.getEclipseExecutable()) : data.getEclipseExecutable() != null) {
            return true;
        }
        if (eclipsePrefs.getText() != null ? !eclipsePrefs.getText().equals(data.getEclipsePrefs()) : data.getEclipsePrefs() != null) {
            return true;
        }
        if (pluginEnabled.isSelected() != data.isPluginEnabled()) {
            return true;
        }
        return false;
    }
}
