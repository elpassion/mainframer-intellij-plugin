package com.elpassion.intelijidea.task.ui;

import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider;
import com.elpassion.intelijidea.task.MFTaskData;
import com.elpassion.intelijidea.task.edit.TaskEditValidatorKt;
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
    public TextFieldWithBrowseButton mainframerToolField;
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
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project);
        mainframerToolField.addBrowseFolderListener(textBrowseFolderListener);
        mainframerToolHolder = new LabeledComponent<>();
        mainframerToolHolder.setComponent(mainframerToolField);
    }

    public Boolean isModified() {
        MFTaskData taskData = settingsProvider.getTaskData();
        boolean configureBeforeTaskOnStartup = settingsProvider.getState().getConfigureBeforeTaskOnStartup();
        return !Comparing.equal(mainframerToolField.getText(), taskData.getMainframerPath()) ||
                !Comparing.equal(buildCommandField.getText(), taskData.getBuildCommand()) ||
                !Comparing.equal(taskNameField.getText(), taskData.getTaskName()) ||
                !Comparing.equal(configureBeforeTasksOnStartupField.isSelected(), configureBeforeTaskOnStartup);
    }

    public void save() {
        MFTaskData taskData = new MFTaskData(
                mainframerToolField.getText(),
                buildCommandField.getText(),
                taskNameField.getText());
        settingsProvider.setTaskData(taskData);
        settingsProvider.getState().setConfigureBeforeTaskOnStartup(configureBeforeTasksOnStartupField.isSelected());
    }

    public void reset() {
        MFTaskData taskData = settingsProvider.getTaskData();
        buildCommandField.setText(taskData.getBuildCommand());
        mainframerToolField.setText(taskData.getMainframerPath());
        taskNameField.setText(taskData.getTaskName());
        configureBeforeTasksOnStartupField.setSelected(settingsProvider.getState().getConfigureBeforeTaskOnStartup());
    }
}
