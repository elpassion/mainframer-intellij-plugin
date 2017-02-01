package com.elpassion.intelijidea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class MFBeforeRunTaskDialog extends DialogWrapper {
    private JPanel contentPane;
    private JTextField commandField;

    protected MFBeforeRunTaskDialog(Project project) {
        super(project);
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

    public void setCommand(String command) {
        commandField.setText(command);
    }

    @NotNull
    public String getCommand() {
        return commandField.getText();
    }
}
