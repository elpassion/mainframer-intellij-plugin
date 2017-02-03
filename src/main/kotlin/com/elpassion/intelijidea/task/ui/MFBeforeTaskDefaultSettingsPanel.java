package com.elpassion.intelijidea.task.ui;

import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.intellij.util.IconUtil;

import javax.swing.*;

public class MFBeforeTaskDefaultSettingsPanel {
    private final Project project;
    private final MFBeforeTaskDefaultSettingsProvider settingsProvider;
    private TextFieldWithBrowseButton mainframerToolField;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerToolHolder;

    public JTextField buildCommandField;
    public JTextField taskNameField;
    public JPanel panel;
    public JCheckBox configureBeforeTasksOnStartupField;

    public MFBeforeTaskDefaultSettingsPanel(Project project, MFBeforeTaskDefaultSettingsProvider settingsProvider) {
        this.project = project;
        this.settingsProvider = settingsProvider;
    }

    private void createUIComponents() {
        mainframerToolField = new TextFieldWithBrowseButton();
        mainframerToolField.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileDescriptor(), project);
        mainframerToolField.addBrowseFolderListener(textBrowseFolderListener);
        mainframerToolHolder = new LabeledComponent<>();
        mainframerToolHolder.setComponent(mainframerToolField);
    }

    public Boolean isModified() {
        return !Comparing.equal(mainframerToolField.getText(), settingsProvider.getDefaultMainframerPath()) ||
                !Comparing.equal(buildCommandField.getText(), settingsProvider.getDefaultBuildCommand()) ||
                !Comparing.equal(taskNameField.getText(), settingsProvider.getDefaultTaskName()) ||
                !Comparing.equal(configureBeforeTasksOnStartupField.isSelected(), settingsProvider.getConfigureBeforeTaskOnStartup());
    }

    public void apply() {
        settingsProvider.setDefaultMainframerPath(mainframerToolField.getText());
        settingsProvider.setDefaultBuildCommand(buildCommandField.getText());
        settingsProvider.setDefaultTaskName(taskNameField.getText());
        settingsProvider.setConfigureBeforeTaskOnStartup(configureBeforeTasksOnStartupField.isSelected());
    }

    public void reset() {
        buildCommandField.setText(settingsProvider.getDefaultBuildCommand());
        mainframerToolField.setText(settingsProvider.getDefaultMainframerPath());
        taskNameField.setText(settingsProvider.getDefaultTaskName());
        configureBeforeTasksOnStartupField.setSelected(settingsProvider.getConfigureBeforeTaskOnStartup());
    }
}
