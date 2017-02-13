package com.elpassion.intelijidea.task.edit;

import com.elpassion.intelijidea.task.MFBeforeRunTask;
import com.elpassion.intelijidea.task.MFTaskData;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.*;
import com.intellij.util.IconUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MFBeforeRunTaskDialog extends DialogWrapper implements TaskEditForm {

    private final TaskEditValidator taskEditValidator = new TaskEditValidator(this);
    private final Project project;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerToolHolder;
    public JPanel panel;
    public JTextField buildCommandField;
    public JTextField taskField;
    public TextFieldWithBrowseButton mainframerToolField;

    public MFBeforeRunTaskDialog(Project project) {
        super(project);
        this.project = project;
        setModal(true);
        init();
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return taskEditValidator.doValidate();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
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

    public MFTaskData createMFTaskDataFromForms() {
        return new MFTaskData(mainframerToolField.getText(),
                buildCommandField.getText(),
                taskField.getText());
    }

    @NotNull
    @Override
    public JTextField taskField() {
        return taskField;
    }

    @NotNull
    @Override
    public JTextField buildCommandField() {
        return buildCommandField;
    }

    @NotNull
    @Override
    public TextFieldWithBrowseButton mainframerToolField() {
        return mainframerToolField;
    }
}
