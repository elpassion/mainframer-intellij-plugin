package com.elpassion.mainframerplugin.task.edit;

import com.elpassion.mainframerplugin.common.ToolDescriptorKt;
import com.elpassion.mainframerplugin.common.ui.InsertMacroActionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.IconUtil;

import javax.swing.*;

public class TaskEditForm {

    private final Project project;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerToolHolder;
    public JPanel panel;
    public JTextField buildCommandField;
    private JButton insertMacroButton;
    public TextFieldWithBrowseButton mainframerToolField;

    public TaskEditForm(Project project) {
        this.project = project;
        insertMacroButton.addActionListener(new InsertMacroActionListener(buildCommandField, project));
    }

    private void createUIComponents() {
        mainframerToolField = new TextFieldWithBrowseButton();
        mainframerToolField.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(ToolDescriptorKt.getToolDescriptor(), project);
        mainframerToolField.addBrowseFolderListener(textBrowseFolderListener);
        mainframerToolHolder = new LabeledComponent<>();
        mainframerToolHolder.setComponent(mainframerToolField);
    }
}
