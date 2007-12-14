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

import com.intellij.ui.DocumentAdapter;
import static net.orfjackal.extformatter.CommandLineCodeFormatter.*;
import net.orfjackal.extformatter.EclipseCodeFormatter;
import net.orfjackal.extformatter.settings.Settings;
import static net.orfjackal.extformatter.settings.Settings.Formatter.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Configuration dialog for changing the {@link Settings} of the plugin.
 *
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class ProjectSettingsForm {

    private static final Color NORMAL = new JTextField().getBackground();
    private static final Color WARNING = new Color(255, 255, 204);
    private static final Color ERROR = new Color(255, 204, 204);

    private JPanel rootComponent;

    private JRadioButton useDefaultFormatter;
    private JRadioButton useEclipseFormatter;
    private JRadioButton useCliFormatter;

    private JTextField  eclipseSupportedFileTypes;
    private JLabel      eclipseSupportedFileTypesLabel;
    private JTextField  eclipseExecutable;
    private JButton     eclipseExecutableBrowse;
    private JLabel      eclipseExecutableLabel;
    private JTextPane   eclipseExecutableExample;
    private JTextField  eclipsePrefs;
    private JButton     eclipsePrefsBrowse;
    private JLabel      eclipsePrefsLabel;
    private JTextPane   eclipsePrefsExample;

    private JTextField  cliSupportedFileTypes;
    private JLabel      cliSupportedFileTypesLabel;
    private JTextPane   cliSupportedFileTypesExample;
    private JTextField  cliReformatOne;
    private JCheckBox   cliReformatOneEnabled;
    private JTextPane   cliReformatOneExample;
    private JTextField  cliReformatMany;
    private JCheckBox   cliReformatManyEnabled;
    private JTextPane   cliReformatManyExample;
    private JTextField  cliReformatDirectory;
    private JCheckBox   cliReformatDirectoryEnabled;
    private JTextPane   cliReformatDirectoryExample;
    private JTextField  cliReformatRecursively;
    private JCheckBox   cliReformatRecursivelyEnabled;
    private JTextPane   cliReformatRecursivelyExample;

    private final List<Popup> visiblePopups = new ArrayList<Popup>();
    @Nullable private File lastDirectory;

    public ProjectSettingsForm() {
        JToggleButton[] modifyableButtons = new JToggleButton[]{
                useDefaultFormatter,
                useEclipseFormatter,
                useCliFormatter,
                cliReformatOneEnabled,
                cliReformatManyEnabled,
                cliReformatDirectoryEnabled,
                cliReformatRecursivelyEnabled,
        };
        for (JToggleButton button : modifyableButtons) {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateComponents();
                }
            });
        }

        JTextField[] modifyableFields = new JTextField[]{
                eclipseExecutable,
                eclipsePrefs,
                cliSupportedFileTypes,
                cliReformatOne,
                cliReformatMany,
                cliReformatDirectory,
                cliReformatRecursively,
        };
        for (JTextField field : modifyableFields) {
            field.getDocument().addDocumentListener(new DocumentAdapter() {
                protected void textChanged(DocumentEvent e) {
                    updateComponents();
                }
            });
        }

        eclipseSupportedFileTypes.setText(EclipseCodeFormatter.SUPPORTED_FILE_TYPES);
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

        rootComponent.addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                // Called when component becomes visible, to ensure that the popups
                // are visible when the form is shown for the first time.
                updateComponents();
            }

            public void ancestorRemoved(AncestorEvent event) {
            }

            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    private void browseForFile(@NotNull JTextField target) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileHidingEnabled(false);    // Eclipse's prefs file is in a hidden ".settings" directory

        if (target.getText().equals("") && lastDirectory != null) {
            chooser.setCurrentDirectory(lastDirectory);
        } else {
            File currentSelection = new File(target.getText());
            chooser.setCurrentDirectory(currentSelection);
            chooser.setSelectedFile(currentSelection);
        }

        int result = chooser.showOpenDialog(rootComponent);
        if (result == JFileChooser.APPROVE_OPTION) {
            target.setText(chooser.getSelectedFile().getAbsolutePath());
        }
        lastDirectory = chooser.getCurrentDirectory();
    }

    private void updateComponents() {
        hidePopups();

        enabledBy(useEclipseFormatter, new JComponent[]{
                eclipseSupportedFileTypesLabel,
                eclipseExecutable,
                eclipseExecutableBrowse,
                eclipseExecutableLabel,
                eclipseExecutableExample,
                eclipsePrefs,
                eclipsePrefsBrowse,
                eclipsePrefsLabel,
                eclipsePrefsExample,
        });
        if (notEmpty(eclipseExecutable) && fileExists(eclipseExecutable)) {
            ok(eclipseExecutable);
        }
        if (notEmpty(eclipsePrefs) && fileExists(eclipsePrefs)) {
            ok(eclipsePrefs);
        }

        enabledBy(useCliFormatter, new JComponent[]{
                cliSupportedFileTypes,
                cliSupportedFileTypesLabel,
                cliSupportedFileTypesExample,
                cliReformatOneEnabled,
                cliReformatManyEnabled,
                cliReformatDirectoryEnabled,
                cliReformatRecursivelyEnabled,
        });
        enabledBy(cliReformatOneEnabled, new JComponent[]{
                cliReformatOne,
                cliReformatOneExample
        });
        enabledBy(cliReformatManyEnabled, new JComponent[]{
                cliReformatMany,
                cliReformatManyExample
        });
        enabledBy(cliReformatDirectoryEnabled, new JComponent[]{
                cliReformatDirectory,
                cliReformatDirectoryExample
        });
        enabledBy(cliReformatRecursivelyEnabled, new JComponent[]{
                cliReformatRecursively,
                cliReformatRecursivelyExample
        });
        if (useCliFormatter.isSelected()) {
            atLeastOneSelected(
                    cliReformatOneEnabled,
                    cliReformatManyEnabled,
                    cliReformatDirectoryEnabled,
                    cliReformatRecursivelyEnabled);
        }
        if (notEmpty(cliSupportedFileTypes)) {
            ok(cliSupportedFileTypes);
        }
        if (notEmpty(cliReformatOne) && containsText(FILE_TAG, cliReformatOne)) {
            ok(cliReformatOne);
        }
        if (notEmpty(cliReformatMany) && containsText(FILES_TAG, cliReformatMany)) {
            ok(cliReformatMany);
        }
        if (notEmpty(cliReformatDirectory) && containsText(DIRECTORY_TAG, cliReformatDirectory)) {
            ok(cliReformatDirectory);
        }
        if (notEmpty(cliReformatRecursively) && containsText(DIRECTORY_TAG, cliReformatRecursively)) {
            ok(cliReformatRecursively);
        }
    }

    private void enabledBy(@NotNull JToggleButton control, @NotNull JComponent[] targets) {
        for (JComponent target : targets) {
            target.setEnabled(control.isEnabled() && control.isSelected());
        }
    }

    private boolean notEmpty(@NotNull JTextField field) {
        if (field.getText().trim().length() == 0) {
            field.setBackground(WARNING);
            showPopup(field, "Required field"); // TODO: localize
            return false;
        }
        return true;
    }

    private boolean containsText(@NotNull String needle, @NotNull JTextField field) {
        if (!field.getText().contains(needle)) {
            field.setBackground(ERROR);
            showPopup(field, "Must contain: " + needle); // TODO: localize
            return false;
        }
        return true;
    }

    private boolean fileExists(@NotNull JTextField field) {
        if (!new File(field.getText()).isFile()) {
            field.setBackground(ERROR);
            showPopup(field, "No such file"); // TODO: localize
            return false;
        }
        return true;
    }

    private void atLeastOneSelected(JToggleButton... buttons) {
        for (JToggleButton button : buttons) {
            if (button.isSelected()) {
                return;
            }
        }
        showPopup(buttons[0], "Select at least one"); // TODO: localize
    }

    private void ok(@NotNull JTextField field) {
        field.setBackground(NORMAL);
    }

    private void showPopup(@NotNull JComponent parent, @NotNull String message) {
        if (!parent.isShowing() || !parent.isEnabled()) {
            return; // if getLocationOnScreen is called when the component is not showing, an exception is thrown
        }
        JToolTip tip = new JToolTip();
        tip.setTipText(message);
        Dimension tipSize = tip.getPreferredSize();

        Point location = parent.getLocationOnScreen();
        int x = (int) location.getX();
        int y = (int) (location.getY() - tipSize.getHeight());

        Popup popup = PopupFactory.getSharedInstance().getPopup(parent, tip, x, y);
        popup.show();
        visiblePopups.add(popup);
    }

    private void hidePopups() {
        for (Iterator<Popup> it = visiblePopups.iterator(); it.hasNext();) {
            Popup popup = it.next();
            popup.hide();
            it.remove();
        }
    }

    @NotNull
    public JPanel getRootComponent() {
        return rootComponent;
    }

    public void importFrom(@NotNull Settings in) {
        useDefaultFormatter.setSelected(in.getFormatter().equals(DEFAULT));
        useEclipseFormatter.setSelected(in.getFormatter().equals(ECLIPSE));
        useCliFormatter.setSelected(in.getFormatter().equals(COMMAND_LINE));

        eclipseExecutable.setText(in.getEclipseExecutable());
        eclipsePrefs.setText(in.getEclipsePrefs());

        cliSupportedFileTypes.setText(in.getCliSupportedFileTypes());
        cliReformatOne.setText(in.getCliReformatOne());
        cliReformatOneEnabled.setSelected(in.isCliReformatOneEnabled());
        cliReformatMany.setText(in.getCliReformatMany());
        cliReformatManyEnabled.setSelected(in.isCliReformatManyEnabled());
        cliReformatDirectory.setText(in.getCliReformatDirectory());
        cliReformatDirectoryEnabled.setSelected(in.isCliReformatDirectoryEnabled());
        cliReformatRecursively.setText(in.getCliReformatRecursively());
        cliReformatRecursivelyEnabled.setSelected(in.isCliReformatRecursivelyEnabled());

        updateComponents();
    }

    public void exportTo(@NotNull Settings out) {
        if (useEclipseFormatter.isSelected()) {
            out.setFormatter(ECLIPSE);
        } else if (useCliFormatter.isSelected()) {
            out.setFormatter(COMMAND_LINE);
        } else {
            out.setFormatter(DEFAULT);
        }

        out.setEclipseExecutable(eclipseExecutable.getText());
        out.setEclipsePrefs(eclipsePrefs.getText());

        out.setCliSupportedFileTypes(cliSupportedFileTypes.getText());
        out.setCliReformatOne(cliReformatOne.getText());
        out.setCliReformatOneEnabled(cliReformatOneEnabled.isSelected());
        out.setCliReformatMany(cliReformatMany.getText());
        out.setCliReformatManyEnabled(cliReformatManyEnabled.isSelected());
        out.setCliReformatDirectory(cliReformatDirectory.getText());
        out.setCliReformatDirectoryEnabled(cliReformatDirectoryEnabled.isSelected());
        out.setCliReformatRecursively(cliReformatRecursively.getText());
        out.setCliReformatRecursivelyEnabled(cliReformatRecursivelyEnabled.isSelected());
    }

    @SuppressWarnings({"RedundantIfStatement", "ConstantConditions"})
    public boolean isModified(Settings data) {
        if (useDefaultFormatter.isSelected() != data.getFormatter().equals(DEFAULT)) {
            return true;
        }
        if (useEclipseFormatter.isSelected() != data.getFormatter().equals(ECLIPSE)) {
            return true;
        }
        if (useCliFormatter.isSelected() != data.getFormatter().equals(COMMAND_LINE)) {
            return true;
        }

        if (eclipseExecutable.getText() != null ? !eclipseExecutable.getText().equals(data.getEclipseExecutable()) : data.getEclipseExecutable() != null) {
            return true;
        }
        if (eclipsePrefs.getText() != null ? !eclipsePrefs.getText().equals(data.getEclipsePrefs()) : data.getEclipsePrefs() != null) {
            return true;
        }

        if (cliSupportedFileTypes.getText() != null ? !cliSupportedFileTypes.getText().equals(data.getCliSupportedFileTypes()) : data.getCliSupportedFileTypes() != null) {
            return true;
        }
        if (cliReformatOne.getText() != null ? !cliReformatOne.getText().equals(data.getCliReformatOne()) : data.getCliReformatOne() != null) {
            return true;
        }
        if (cliReformatOneEnabled.isSelected() != data.isCliReformatOneEnabled()) {
            return true;
        }
        if (cliReformatMany.getText() != null ? !cliReformatMany.getText().equals(data.getCliReformatMany()) : data.getCliReformatMany() != null) {
            return true;
        }
        if (cliReformatManyEnabled.isSelected() != data.isCliReformatManyEnabled()) {
            return true;
        }
        if (cliReformatDirectory.getText() != null ? !cliReformatDirectory.getText().equals(data.getCliReformatDirectory()) : data.getCliReformatDirectory() != null) {
            return true;
        }
        if (cliReformatDirectoryEnabled.isSelected() != data.isCliReformatDirectoryEnabled()) {
            return true;
        }
        if (cliReformatRecursively.getText() != null ? !cliReformatRecursively.getText().equals(data.getCliReformatRecursively()) : data.getCliReformatRecursively() != null) {
            return true;
        }
        if (cliReformatRecursivelyEnabled.isSelected() != data.isCliReformatRecursivelyEnabled()) {
            return true;
        }
        return false;
    }
}
