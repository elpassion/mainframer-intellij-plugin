package com.elpassion.mainframerplugin.configuration.ui;

import com.elpassion.mainframerplugin.common.ui.InsertMacroActionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.IconUtil;

import javax.swing.*;

import static com.elpassion.mainframerplugin.common.ToolDescriptorKt.getToolDescriptor;

public class SettingsEditorPanel {
    private final Project project;
    public JTextField buildCommand;
    public JPanel panel;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerToolHolder;
    private JButton insertMacroButton;
    public TextFieldWithBrowseButton mainframerTool;

    public SettingsEditorPanel(Project project) {
        this.project = project;
    }

    private void createUIComponents() {
        mainframerTool = new TextFieldWithBrowseButton();
        mainframerTool.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(getToolDescriptor(), project);
        mainframerTool.addBrowseFolderListener(textBrowseFolderListener);
        mainframerToolHolder = new LabeledComponent<>();
        mainframerToolHolder.setComponent(mainframerTool);
        insertMacroButton.addActionListener(new InsertMacroActionListener(buildCommand, project));
    }
}
