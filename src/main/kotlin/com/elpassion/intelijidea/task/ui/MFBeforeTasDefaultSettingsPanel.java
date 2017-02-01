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

public class MFBeforeTasDefaultSettingsPanel {
    private final Project project;
    private final MFBeforeTaskDefaultSettingsProvider settingsProvider;

    public JTextField buildCommandField;
    public JTextField taskNameField;
    public JPanel panel;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerScriptHolder;
    public TextFieldWithBrowseButton mainframerScriptField;

    public MFBeforeTasDefaultSettingsPanel(Project project, MFBeforeTaskDefaultSettingsProvider settingsProvider) {
        this.project = project;
        this.settingsProvider = settingsProvider;
    }

    private void createUIComponents() {
        mainframerScriptField = new TextFieldWithBrowseButton();
        mainframerScriptField.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileDescriptor(), project);
        mainframerScriptField.addBrowseFolderListener(textBrowseFolderListener);
        mainframerScriptHolder = new LabeledComponent<>();
        mainframerScriptHolder.setComponent(mainframerScriptField);
    }

    public Boolean isModified() {
        return !Comparing.equal(mainframerScriptField.getText(), settingsProvider.getDefaultMainframerScript()) ||
                !Comparing.equal(buildCommandField.getText(), settingsProvider.getDefaultBuildCommand()) ||
                !Comparing.equal(taskNameField.getText(), settingsProvider.getDefaultTaskName());
    }

    public void apply() {
        settingsProvider.setDefaultMainframerScript(mainframerScriptField.getText());
        settingsProvider.setDefaultBuildCommand(buildCommandField.getText());
        settingsProvider.setDefaultTaskName(taskNameField.getText());
    }

    public void reset() {
        buildCommandField.setText(settingsProvider.getDefaultMainframerScript());
        mainframerScriptField.setText(settingsProvider.getDefaultBuildCommand());
        taskNameField.setText(settingsProvider.getDefaultTaskName());
    }
}
