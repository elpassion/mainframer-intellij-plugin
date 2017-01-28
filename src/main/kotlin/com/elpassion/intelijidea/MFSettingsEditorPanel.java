package com.elpassion.intelijidea;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.IconUtil;

import javax.swing.*;

public class MFSettingsEditorPanel {
    private final Project project;
    public JTextField taskName;
    public JPanel panel;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerScriptHolder;
    public TextFieldWithBrowseButton mainframerScript;

    public MFSettingsEditorPanel(Project project) {
        this.project = project;
    }

    private void createUIComponents() {
        mainframerScript = new TextFieldWithBrowseButton();
        mainframerScript.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileDescriptor(), project);
        mainframerScript.addBrowseFolderListener(textBrowseFolderListener);
        mainframerScriptHolder = new LabeledComponent<>();
        mainframerScriptHolder.setComponent(mainframerScript);
    }
}
