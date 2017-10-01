package com.elpassion.mainframerplugin.task.ui;

import com.elpassion.mainframerplugin.common.*;
import com.elpassion.mainframerplugin.common.ui.InsertMacroActionListener;
import com.elpassion.mainframerplugin.task.MainframerTaskDefaultSettingsProvider;
import com.elpassion.mainframerplugin.task.TaskData;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.Comparing;
import com.intellij.util.IconUtil;

import javax.swing.*;

public class TaskDefaultSettingsPanel {
    private final Project project;
    private final MainframerTaskDefaultSettingsProvider settingsProvider;
    public TextFieldWithBrowseButton mainframerToolField;
    public JTextField buildCommandField;
    public JPanel panel;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerToolHolder;
    public JTextField remoteMachineField;
    private JButton insertMacroButton;
    private ToolConfiguration toolConfiguration;

    public TaskDefaultSettingsPanel(Project project, MainframerTaskDefaultSettingsProvider settingsProvider, ToolConfiguration toolConfiguration) {
        this.project = project;
        this.settingsProvider = settingsProvider;
        this.toolConfiguration = toolConfiguration;
    }

    private void createUIComponents() {
        mainframerToolField = new TextFieldWithBrowseButton();
        mainframerToolField.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(ToolDescriptorKt.getToolDescriptor(), project);
        mainframerToolField.addBrowseFolderListener(textBrowseFolderListener);
        mainframerToolHolder = new LabeledComponent<>();
        mainframerToolHolder.setComponent(mainframerToolField);
        insertMacroButton.addActionListener(new InsertMacroActionListener(buildCommandField, project));
    }

    public Boolean isModified() {
        final TaskData taskData = settingsProvider.getTaskData();
        final String remoteMachineName = toolConfiguration.readRemoteMachineName();
        return !Comparing.equal(mainframerToolField.getText(), taskData.getMainframerPath()) ||
                !Comparing.equal(buildCommandField.getText(), taskData.getBuildCommand()) ||
                !Comparing.equal(remoteMachineField.getText(), remoteMachineName);
    }

    private void save() {
        TaskData taskData = new TaskData(
                buildCommandField.getText(), mainframerToolField.getText()
        );
        settingsProvider.setTaskData(taskData);
        toolConfiguration.writeRemoteMachineName(remoteMachineField.getText());
    }

    public void reset() {
        TaskData taskData = settingsProvider.getTaskData();
        buildCommandField.setText(taskData.getBuildCommand());
        mainframerToolField.setText(taskData.getMainframerPath());
        remoteMachineField.setText(toolConfiguration.readRemoteMachineName());
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
                new BuildCommandValidator(buildCommandField),
                new MainframerPathValidator(mainframerToolField));
    }
}
