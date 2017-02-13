package com.elpassion.intelijidea.task.edit;

import com.elpassion.intelijidea.task.MFBeforeRunTask;
import com.elpassion.intelijidea.task.MFTaskData;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
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
    public JTextField taskField;
    public TextFieldWithBrowseButton mainframerToolField;

    public MFBeforeRunTaskForm(Project project) {
        this.project = project;
    }

    private void createUIComponents() {
        mainframerToolField = new TextFieldWithBrowseButton();
        mainframerToolField.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project);
        mainframerToolField.addBrowseFolderListener(textBrowseFolderListener);
        mainframerToolHolder = new LabeledComponent<>();
        mainframerToolHolder.setComponent(mainframerToolField);
    }

    public void restoreMainframerTask(MFBeforeRunTask beforeRunTask) {
        MFTaskData data = beforeRunTask.getData();
        mainframerToolField.setText(data.getMainframerPath());
        buildCommandField.setText(data.getBuildCommand());
        taskField.setText(data.getTaskName());
    }

}
