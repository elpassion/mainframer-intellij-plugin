package com.elpassion.intelijidea.task.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.IconUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MFBeforeRunTaskDialog extends DialogWrapper {
    private final Project project;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerScriptHolder;
    private JPanel contentPane;

    public JTextField buildCommandField;
    public JTextField taskField;
    public TextFieldWithBrowseButton mainframerScriptField;

    public MFBeforeRunTaskDialog(Project project) {
        super(project);
        this.project = project;
        setModal(true);
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    private void createUIComponents() {
        mainframerScriptField = new TextFieldWithBrowseButton();
        mainframerScriptField.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileDescriptor(), project);
        mainframerScriptField.addBrowseFolderListener(textBrowseFolderListener);
        mainframerScriptHolder = new LabeledComponent<>();
        mainframerScriptHolder.setComponent(mainframerScriptField);
    }
}
