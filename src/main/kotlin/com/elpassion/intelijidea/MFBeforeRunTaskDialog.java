package com.elpassion.intelijidea;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.IconUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class MFBeforeRunTaskDialog extends DialogWrapper {
    private final Project project;
    private LabeledComponent<TextFieldWithBrowseButton> mainframerScriptHolder;
    private JPanel contentPane;

    public JTextField commandField;
    public JTextField taskField;
    public TextFieldWithBrowseButton mainframerScript;

    protected MFBeforeRunTaskDialog(Project project) {
        super(project);
        this.project = project;
        setModal(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    private void onCancel() {
        dispose();
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
