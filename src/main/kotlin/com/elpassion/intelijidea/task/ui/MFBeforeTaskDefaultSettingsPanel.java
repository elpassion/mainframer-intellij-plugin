package com.elpassion.intelijidea.task.ui;

import com.elpassion.intelijidea.common.*;
import com.elpassion.intelijidea.task.MFBeforeTaskDefaultSettingsProvider;
import com.elpassion.intelijidea.task.MFTaskData;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.Comparing;
import com.intellij.util.IconUtil;

import javax.swing.*;

public class MFBeforeTaskDefaultSettingsPanel {
    private final Project project;
    private final MFBeforeTaskDefaultSettingsProvider settingsProvider;
    public TextFieldWithBrowseButton mainframerToolField;
    public JTextField buildCommandField;
    public JTextField taskNameField;
    public JPanel panel;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerToolHolder;
    public JTextField remoteMachineField;
    private MFToolConfiguration mfToolConfiguration;

    public MFBeforeTaskDefaultSettingsPanel(Project project, MFBeforeTaskDefaultSettingsProvider settingsProvider, MFToolConfiguration mfToolConfiguration) {
        this.project = project;
        this.settingsProvider = settingsProvider;
        this.mfToolConfiguration = mfToolConfiguration;
    }

    private void createUIComponents() {
        mainframerToolField = new TextFieldWithBrowseButton();
        mainframerToolField.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(MFToolFileDescriptorKt.getMfToolDescriptor(), project);
        mainframerToolField.addBrowseFolderListener(textBrowseFolderListener);
        mainframerToolHolder = new LabeledComponent<>();
        mainframerToolHolder.setComponent(mainframerToolField);
    }

    public Boolean isModified() {
        final MFTaskData taskData = settingsProvider.getTaskData();
        final String remoteMachineName = mfToolConfiguration.readRemoteMachineName();
        return !Comparing.equal(mainframerToolField.getText(), taskData.getMainframerPath()) ||
                !Comparing.equal(buildCommandField.getText(), taskData.getBuildCommand()) ||
                !Comparing.equal(taskNameField.getText(), taskData.getTaskName()) ||
                !Comparing.equal(remoteMachineField.getText(), remoteMachineName);
    }

    private void save() {
        MFTaskData taskData = new MFTaskData(
                mainframerToolField.getText(),
                buildCommandField.getText(),
                taskNameField.getText());
        settingsProvider.setTaskData(taskData);
        mfToolConfiguration.writeRemoteMachineName(remoteMachineField.getText());
    }

    public void reset() {
        MFTaskData taskData = settingsProvider.getTaskData();
        buildCommandField.setText(taskData.getBuildCommand());
        mainframerToolField.setText(taskData.getMainframerPath());
        taskNameField.setText(taskData.getTaskName());
        remoteMachineField.setText(mfToolConfiguration.readRemoteMachineName());
    }

    public void apply() throws ConfigurationException {
        ValidationInfo validationInfo = validate();
        if (validationInfo != null) {
            throw new ConfigurationException(validationInfo.message);
        } else {
            save();
        }
    }

    private ValidationInfo validate() {
        return FormValidatorKt.validateForm(
                new TaskFieldValidator(taskNameField),
                new BuildCommandValidator(buildCommandField),
                new MainframerPathValidator(mainframerToolField));
    }
}
