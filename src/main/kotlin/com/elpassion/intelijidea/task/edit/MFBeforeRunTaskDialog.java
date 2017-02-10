package com.elpassion.intelijidea.task.edit;

import com.elpassion.intelijidea.util.JTextFieldExtensionKt;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.IconUtil;
import io.reactivex.Observable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MFBeforeRunTaskDialog extends DialogWrapper implements TaskEditView, TaskEditView.Actions {
    private final Project project;
    private final TaskEditController controller = new TaskEditController(this, this);
    private LabeledComponent<TextFieldWithBrowseButton> mainframerToolHolder;
    private JPanel contentPane;

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
    protected JComponent createCenterPanel() {
        controller.onCreate();
        return contentPane;
    }

    private void createUIComponents() {
        mainframerToolField = new TextFieldWithBrowseButton();
        mainframerToolField.setButtonIcon(IconUtil.getAddIcon());
        TextBrowseFolderListener textBrowseFolderListener = new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project);
        mainframerToolField.addBrowseFolderListener(textBrowseFolderListener);
        mainframerToolHolder = new LabeledComponent<>();
        mainframerToolHolder.setComponent(mainframerToolField);
    }

    @Override
    public Observable<String> observeTaskName() {
        return JTextFieldExtensionKt.textChanges(taskField);
    }

    @Override
    public void enableAcceptButton() {
        setOKActionEnabled(true);
    }

    @Override
    public void disableAcceptButton() {
        setOKActionEnabled(false);
    }
}
