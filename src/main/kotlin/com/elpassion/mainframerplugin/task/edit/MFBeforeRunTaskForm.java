package com.elpassion.mainframerplugin.task.edit;

import com.elpassion.mainframerplugin.common.MFToolFileDescriptorKt;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.IconUtil;

import javax.swing.*;

public class MFBeforeRunTaskForm {

    private final Project project;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerToolHolder;
    public JPanel panel;
    public JTextField buildCommandField;
    public TextFieldWithBrowseButton mainframerToolField;

    public MFBeforeRunTaskForm(Project project) {
        this.project = project;
    }

    private void createUIComponents() {
        mainframerToolField = new TextFieldWithBrowseButton();
        mainframerToolField.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(MFToolFileDescriptorKt.getMfToolDescriptor(), project);
        mainframerToolField.addBrowseFolderListener(textBrowseFolderListener);
        mainframerToolHolder = new LabeledComponent<>();
        mainframerToolHolder.setComponent(mainframerToolField);
    }
}
