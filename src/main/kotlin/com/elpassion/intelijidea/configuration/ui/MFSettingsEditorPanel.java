package com.elpassion.intelijidea.configuration.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.IconUtil;

import javax.swing.*;

public class MFSettingsEditorPanel {
    private final Project project;
    public JTextField buildCommand;
    public JTextField taskName;
    public JPanel panel;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerToolHolder;
    public TextFieldWithBrowseButton mainframerTool;

    public MFSettingsEditorPanel(Project project) {
        this.project = project;
    }

    private void createUIComponents() {
        mainframerTool = new TextFieldWithBrowseButton();
        mainframerTool.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileDescriptor(), project);
        mainframerTool.addBrowseFolderListener(textBrowseFolderListener);
        mainframerToolHolder = new LabeledComponent<>();
        mainframerToolHolder.setComponent(mainframerTool);
    }
}
